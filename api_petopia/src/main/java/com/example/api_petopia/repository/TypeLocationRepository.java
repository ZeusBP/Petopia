package com.example.api_petopia.repository;

import com.example.api_petopia.entity.Location;
import com.example.api_petopia.entity.TypeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeLocationRepository extends JpaRepository<TypeLocation,Integer> {
}
