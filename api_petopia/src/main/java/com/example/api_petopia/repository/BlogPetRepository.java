package com.example.api_petopia.repository;

import com.example.api_petopia.entity.BlogPet;

import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface BlogPetRepository extends JpaRepository<BlogPet, Long>, JpaSpecificationExecutor<BlogPet> {
    Page<BlogPet> findAllByStatusEquals( BlogPetStatus status, Pageable pageable);

    List<BlogPet> findAllByUserId(Long userId);
    List<BlogPet> findAllByStatus(BlogPetStatus status);


}
