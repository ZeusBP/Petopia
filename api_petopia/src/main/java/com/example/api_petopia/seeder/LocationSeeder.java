package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.Location;
import com.example.api_petopia.entity.TypeLocation;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.LocationStatus;
import com.example.api_petopia.repository.LocationRepository;
import com.example.api_petopia.util.LocalDateTimeHelper;
import com.example.api_petopia.util.NumberUtil;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LocationSeeder {
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    TypeLocationSeeder typeLocationSeeder;

    @Autowired
    UserSeeder userSeeder;

    Faker faker = new Faker();
    List<SeedByTime> seeder;

    public static final int NUMBER_OF_LOCATION = 20;

    public static List<Location> locationList = new ArrayList<>();

    public void configData() {
        seeder = new ArrayList<>();
        seeder.add(SeedByTime.builder()
                .blogPetStatus(BlogPetStatus.ACTIVE).seedTypeByTime(SeedByTimeType.YEAR).year(2022)
                .build());
        seeder.add(SeedByTime.builder()
                .blogPetStatus(BlogPetStatus.ACTIVE).seedTypeByTime(SeedByTimeType.YEAR).year(2021).month(Month.DECEMBER)
                .build());
        seeder.add(SeedByTime.builder()
                .blogPetStatus(BlogPetStatus.DEACTIVE).seedTypeByTime(SeedByTimeType.MONTH).month(Month.SEPTEMBER).year(2022)
                .build());
    }

    public void generate() {
        configData();
        Set<String> mapLocation = new HashSet<>();
        for (SeedByTime seedByTime : seeder) {
            for (int i = 0; i < NUMBER_OF_LOCATION; i++) {
                Location location = new Location();

                int randomTypeLocation = faker.number().numberBetween(0, typeLocationSeeder.typeLocationList.size() - 1);
                TypeLocation typeLocation = typeLocationSeeder.typeLocationList.get(randomTypeLocation);
                location.setTypeLocation(typeLocation);

                int randomUser = faker.number().numberBetween(0, userSeeder.userList.size() - 1);
                User user = userSeeder.userList.get(randomUser);
                location.setUser(user);

                String nameLocation;
                do {
                    nameLocation = faker.name().title();
                } while (mapLocation.contains(nameLocation));
                location.setNameLocation(nameLocation);
                mapLocation.add(nameLocation);
                location.setThumbnail("https://picsum.photos/200/300?random="+i);
                location.setAddress(faker.address().fullAddress());
                location.setPhone(faker.phoneNumber().phoneNumber());
                location.setEmail(faker.internet().emailAddress());
                location.setDescription(faker.lorem().sentence());
                LocalDateTime createdTime = calculatorCreateTime(seedByTime);
                location.setCreatedAt(createdTime);
                int randomStatus = faker.number().numberBetween(0, 2);
                if (randomStatus == 1) {
                    location.setStatus(LocationStatus.ACTIVE);
                } else if (randomStatus == 0) {
                    location.setStatus(LocationStatus.DEACTIVE);
                }
                locationList.add(location);
            }
        }
        locationRepository.saveAll(locationList);
    }

    private LocalDateTime calculatorCreateTime(SeedByTime seedByTime) {
        LocalDateTime result = null;
        LocalDateTime tempLocalDateTime = null;
        int tempMonth = 1;
        int tempYear = 2022;
        switch (seedByTime.getSeedTypeByTime()) {
            case YEAR:
                tempMonth = LocalDateTimeHelper.getRandomMonth().getValue();
                tempYear = seedByTime.getYear();
                tempLocalDateTime = LocalDateTime.of(tempYear, tempMonth, 1, 9, 30, 0);
                result = tempLocalDateTime.plusMonths(1).minusDays(1);
                break;
            case MONTH:
                tempMonth = seedByTime.getMonth().getValue();
                tempYear = seedByTime.getYear();
                tempLocalDateTime = LocalDateTime.of(tempYear, tempMonth, 1, 18, 0, 0);
                LocalDateTime lastDayOfMonth = tempLocalDateTime.plusMonths(1).minusDays(1);
                int randomDay = NumberUtil.getRandomNumber(1, lastDayOfMonth.getDayOfMonth());
                result = LocalDateTime.of(tempYear, tempMonth, randomDay, 20, 0, 0);
                if (result.isAfter(LocalDateTime.now())) {
                    // nếu sau thời gian hiện tại, tức là tháng năm đang thời gian hiện tại
                    randomDay = NumberUtil.getRandomNumber(1, LocalDateTime.now().getDayOfMonth());
                    result = LocalDateTime.of(tempYear, tempMonth, randomDay, 10, 0, 0);
                }
                break;
            case DAY:
                // nếu là ngày thì fix
                result = LocalDateTime.of(seedByTime.getYear(), seedByTime.getMonth(), seedByTime.getDay(), 10, 20, 0);
                break;
        }
        return result;
    }
}
