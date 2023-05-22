package com.example.api_petopia.service;

import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.repository.CatePetRepository;
import com.example.api_petopia.repository.TypePetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypePetService {
    @Autowired
    private TypePetRepository typePetRepository;

    public List<TypePet> findAll() {
        return typePetRepository.findAll();
    }
}
