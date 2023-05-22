package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.Location;
import com.example.api_petopia.entity.TypeLocation;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.repository.TypeLocationRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TypeLocationSeeder {
    @Autowired
    TypeLocationRepository typeLocationRepository;

    public static final int NUMBER_OF_TYPELOCATION = 15;
    public static List<TypeLocation> typeLocationList = new ArrayList<>();
    Faker faker = new Faker();

    public void generate(){
//        Set<String> mapTypeLocation = new HashSet<>();
//        for (int i = 0; i < NUMBER_OF_TYPELOCATION; i++){
//            TypeLocation typeLocation = new TypeLocation();
//            String typeName;
//            do {
//                typeName = faker.job().title();
//            }while (mapTypeLocation.contains(typeName));
//            typeLocation.setName(typeName);
//            typeLocationList.add(typeLocation);
//            mapTypeLocation.add(typeLocation.getName());
//        }
        TypeLocation typeLocation = new TypeLocation("Day Care");
        TypeLocation typeLocation1 = new TypeLocation("Pet Spa");
        TypeLocation typeLocation2 = new TypeLocation("Vaccine");
        TypeLocation typeLocation3 = new TypeLocation("Training");
        TypeLocation typeLocation5 = new TypeLocation("Vets");


        typeLocationList.add(typeLocation);
        typeLocationList.add(typeLocation1);
        typeLocationList.add(typeLocation2);
        typeLocationList.add(typeLocation3);
        typeLocationList.add(typeLocation5);

        typeLocationRepository.saveAll(typeLocationList);
    }
}
