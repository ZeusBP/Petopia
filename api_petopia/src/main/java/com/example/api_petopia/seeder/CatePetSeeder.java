package com.example.api_petopia.seeder;


import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.repository.CatePetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatePetSeeder {
    @Autowired
    CatePetRepository catePetRepository;

    public static List<CatePet> catePetList = new ArrayList<>();

    public void generate(){
//        for ( int i = 0;i<=10;i++){
//            TypePet typePet1 = new TypePet("Dog"+i);
//            typePetList.add(typePet1);
//        }

        CatePet catePet = new CatePet("LostPet");
        CatePet catePet1 = new CatePet("Adoption");

        catePetList.add(catePet);
        catePetList.add(catePet1);
        catePetRepository.saveAll(catePetList);

    }
}
