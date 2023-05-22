package com.example.api_petopia.repository;

import com.example.api_petopia.entity.TypePet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePetRepository extends JpaRepository<TypePet, Integer> {
}
