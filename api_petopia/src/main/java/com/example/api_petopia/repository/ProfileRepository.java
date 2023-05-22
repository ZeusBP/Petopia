package com.example.api_petopia.repository;

import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    Optional<User> findByUsernameAndStatus(String username, UserStatus status);
}
