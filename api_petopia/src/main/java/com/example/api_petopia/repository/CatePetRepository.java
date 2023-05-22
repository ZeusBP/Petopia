package com.example.api_petopia.repository;

import com.example.api_petopia.entity.CatePet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatePetRepository extends JpaRepository<CatePet, Integer> {
}
