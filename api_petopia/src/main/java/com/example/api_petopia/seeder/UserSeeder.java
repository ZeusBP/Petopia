package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.Role;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.example.api_petopia.repository.RoleRepository;
import com.example.api_petopia.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserSeeder {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Faker faker = new Faker();

    public static final int NUMBER_OF_USER = 50;


    public static List<User> userList = new ArrayList<>();
    public static List<Role> roleList = new ArrayList<>();

    public void generate(){
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");
        roleList.add(adminRole);
        roleList.add(userRole);
        roleRepository.saveAll(roleList);
        userList.add(User
                .builder()
                .username("admin1")
                .password(passwordEncoder.encode("admin1"))
                .fullName("Pham Dat")
                .address("Thanh Hoa")
                .phone("086646236")
                .email("datyasuo202@gmail.com")
                .thumbnailAvt("https://as1.ftcdn.net/v2/jpg/03/53/11/00/1000_F_353110097_nbpmfn9iHlxef4EDIhXB1tdTD0lcWhG9.jpg?fbclid=IwAR0IeeX4fdIKXrmKyVLdn3mGEhAkNFdQv3MH7f4P5okIBtG_Rx_fqonZjss")
                .role(adminRole)
                .status(UserStatus.ACTIVE)
                .build());

        userList.add(User
                .builder()
                .username("user1")
                .password(passwordEncoder.encode("user1"))
                .fullName("Pham Dat")
                .address("Thanh Hoa")
                .thumbnailAvt("https://as1.ftcdn.net/v2/jpg/03/53/11/00/1000_F_353110097_nbpmfn9iHlxef4EDIhXB1tdTD0lcWhG9.jpg?fbclid=IwAR0IeeX4fdIKXrmKyVLdn3mGEhAkNFdQv3MH7f4P5okIBtG_Rx_fqonZjss")
                .phone("086646236")
                .email("datyasuo202@gmail.com")
                .role(userRole)
                .status(UserStatus.ACTIVE)
                .build());

        userList.add(User
                .builder()
                .username("user2")
                .password(passwordEncoder.encode("user2"))
                .fullName("Pham Dat")
                .address("Thanh Hoa")
                .thumbnailAvt("https://as1.ftcdn.net/v2/jpg/03/53/11/00/1000_F_353110097_nbpmfn9iHlxef4EDIhXB1tdTD0lcWhG9.jpg?fbclid=IwAR0IeeX4fdIKXrmKyVLdn3mGEhAkNFdQv3MH7f4P5okIBtG_Rx_fqonZjss")
                .phone("086646236")
                .email("datyasuo202@gmail.com")
                .role(userRole)
                .status(UserStatus.ACTIVE)
                .build());

        for (int i = 0; i < NUMBER_OF_USER; i++){
            User user = new User();
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            int randomStatus = faker.number().numberBetween(0, 4);
            if (randomStatus == 1) {
                user.setStatus(UserStatus.ACTIVE);
            } else if (randomStatus == 0) {
                user.setStatus(UserStatus.BLOCKED);
            }
            else if (randomStatus == 2) {
                user.setStatus(UserStatus.WARNING);
            }
            else if (randomStatus == 3) {
                user.setStatus(UserStatus.DELETED);
            }
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setRole(userRole);
            user.setThumbnailAvt("https://picsum.photos/200/300?random="+i);
            user.setUsername(faker.name().username());
            user.setPhone(faker.phoneNumber().cellPhone());
            user.setFullName(faker.name().fullName());
            user.setAddress(faker.address().fullAddress());
            user.setEmail(faker.lorem().characters(10) + "@gmail.com");
            userList.add(user);
        }
        userRepository.saveAll(userList);
    }
}
