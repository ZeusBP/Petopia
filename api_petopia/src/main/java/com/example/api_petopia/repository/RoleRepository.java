package com.example.api_petopia.repository;

import com.example.api_petopia.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

   Role findByName(String name);}
