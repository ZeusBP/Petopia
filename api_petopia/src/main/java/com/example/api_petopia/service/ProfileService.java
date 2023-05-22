package com.example.api_petopia.service;

import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.example.api_petopia.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;

    public Optional<User> findById(Long id){
        return profileRepository.findById(id);
    }

    public User save(User user) {
        return profileRepository.save(user);
    }

    public User findByUsername(String username) {
        return profileRepository.findByUsername(username);
    }
    public Optional<User> findByNameActive(String username){
        return profileRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
    }
}
