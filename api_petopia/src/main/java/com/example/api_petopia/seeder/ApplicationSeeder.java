package com.example.api_petopia.seeder;

import com.example.api_petopia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSeeder implements CommandLineRunner {
    @Autowired
    UserSeeder userSeeder;
    @Autowired
    CategorySeeder categorySeeder;
    @Autowired
    TypePetSeeder typePetSeeder;
    @Autowired
    CatePetSeeder catePetSeeder;

    @Autowired
    BlogSeeder blogSeeder;
    @Autowired
    TypeLocationSeeder typeLocationSeeder;

    @Autowired
    LocationSeeder locationSeeder;
    @Autowired
    ProductSeeder productSeeder;


    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TypePetRepository typePetRepository;
    @Autowired
    CatePetRepository catePetRepository;

    @Autowired
    BlogPetRepository blogPetRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    TypeLocationRepository typeLocationRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderSeeder orderSeeder;

    boolean seed = true;

    @Override
    public void run(String... args) throws Exception {
        if (seed){
            userRepository.deleteAll();
            typePetRepository.deleteAll();
            categoryRepository.deleteAll();
            catePetRepository.deleteAll();
            blogPetRepository.deleteAll();
            typeLocationRepository.deleteAll();
            locationRepository.deleteAll();
            productRepository.deleteAll();
            orderRepository.deleteAll();

            userSeeder.generate();
            typePetSeeder.generate();
            categorySeeder.generate();
            catePetSeeder.generate();
            blogSeeder.generate();
            typeLocationSeeder.generate();
            locationSeeder.generate();
            productSeeder.generate();
            orderSeeder.generate();
            orderSeeder.generateOrderDetail();

        }
    }
}
