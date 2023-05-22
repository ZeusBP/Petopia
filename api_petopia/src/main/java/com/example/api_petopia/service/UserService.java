package com.example.api_petopia.service;

import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.Role;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.dto.CredentialDto;
import com.example.api_petopia.entity.dto.UserDto;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.example.api_petopia.repository.RoleRepository;
import com.example.api_petopia.repository.UserRepository;
import com.example.api_petopia.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    final PasswordEncoder passwordEncoder;

    public boolean matchPassword(String rawPassword, String hashPassword) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
        User user = userOptional.orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
        authorities.add(authority);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public CredentialDto generateCredential(UserDetails userDetail, HttpServletRequest request) {
        String accessToken = JWTUtil.generateToken(userDetail.getUsername(),
                userDetail.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURI(),
                JWTUtil.ONE_DAY * 7);

        String refreshToken = JWTUtil.generateToken(userDetail.getUsername(),
                userDetail.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURI(),
                JWTUtil.ONE_DAY * 14);
        return new CredentialDto(accessToken, refreshToken);
    }

    public User register(UserDto userDto) {
        Role role = roleRepository.findByName("USER");
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .fullName("User"+ UUID.randomUUID())
                .address(null)
                .phone(null)
                .email(userDto.getEmail())
                .thumbnailAvt("https://as1.ftcdn.net/v2/jpg/03/53/11/00/1000_F_353110097_nbpmfn9iHlxef4EDIhXB1tdTD0lcWhG9.jpg?fbclid=IwAR0IeeX4fdIKXrmKyVLdn3mGEhAkNFdQv3MH7f4P5okIBtG_Rx_fqonZjss")
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);

    }

    public User getUser(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        return byUsername.orElse(null);
    }

    public Optional<User> findByNameActive(String username) {
        return userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
    public User save(User user) {
        return userRepository.save(user);
    }

    public Page<User> searchAll(Specification<User> specification,int page, int limit){
        return userRepository.findAll(specification,PageRequest.of(page, limit));
    }

    public int totalUser(){
        return userRepository.findAll().size();
    }

    public int totalUserByStatus(int status){
        UserStatus status1 = UserStatus.ACTIVE;
        if (status == 0){
            status1 = UserStatus.BLOCKED;
        }if (status == 2){
            status1 = UserStatus.WARNING;
        }
        if (status == 3){
            status1 = UserStatus.DELETED;
        }
        return userRepository.findAllByStatus(status1).size();
    }

    public int totalUserByRole(Integer roleId){
        Optional<Role> role = roleRepository.findById(roleId);
        return userRepository.findAllByRole(role.get()).size();
    }

}
