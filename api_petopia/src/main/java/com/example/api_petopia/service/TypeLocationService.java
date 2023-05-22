package com.example.api_petopia.service;

import com.example.api_petopia.entity.TypeLocation;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.repository.TypeLocationRepository;
import com.example.api_petopia.repository.TypePetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeLocationService {
    @Autowired
    private TypeLocationRepository typeLocationRepository;

    public List<TypeLocation> findAll() {
        return typeLocationRepository.findAll();
    }
}
