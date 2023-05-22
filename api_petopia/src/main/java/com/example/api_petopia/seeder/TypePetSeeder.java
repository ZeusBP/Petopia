package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.repository.TypePetRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TypePetSeeder {
    @Autowired
    TypePetRepository typePetRepository;

    public static final int NUMBER_OF_TYPEPET = 7;

    Faker faker = new Faker();
    public static List<TypePet> typePetList = new ArrayList<>();

    public void generate(){
//        for ( int i = 0;i<=10;i++){
//            TypePet typePet1 = new TypePet("Dog"+i);
//            typePetList.add(typePet1);
//        }

        TypePet typePet = new TypePet("Dog");
        TypePet typePet1 = new TypePet("Cat");
        TypePet typePet2 = new TypePet("Bird");
        TypePet typePet3 = new TypePet("Rabbit");
        TypePet typePet4 = new TypePet("Hamster");
        TypePet typePet5 = new TypePet("Other");

        typePetList.add(typePet);
        typePetList.add(typePet1);
        typePetList.add(typePet2);
        typePetList.add(typePet3);
        typePetList.add(typePet4);
        typePetList.add(typePet5);

//        Set<String> mapTypePet = new HashSet<>();
//        for (int i = 0; i < NUMBER_OF_TYPEPET; i++){
//            TypePet typePet3 = new TypePet();
//            String typepet;
//            do {
//                typepet = faker.animal().name();
//            }while (mapTypePet.contains(typepet));
//            typePet3.setName(typepet);
//            typePetList.add(typePet3);
//            mapTypePet.add(typePet3.getName());
//        }
        typePetRepository.saveAll(typePetList);

    }
}
