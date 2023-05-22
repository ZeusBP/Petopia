package com.example.api_petopia.repository;

import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.Role;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndStatus(String username, UserStatus status);
    Optional<User> findByIdAndStatus(Long userId, UserStatus status);

    List<User> findAllByStatus(UserStatus status);

    List<User> findAllByRole(Role role);

}
