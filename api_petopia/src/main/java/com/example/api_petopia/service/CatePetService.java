package com.example.api_petopia.service;

import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.repository.CatePetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatePetService {
    @Autowired
    private CatePetRepository catePetRepository;

    public List<CatePet> findAll() {
        return catePetRepository.findAll();
    }
}
