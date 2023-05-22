package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.repository.BlogPetRepository;
import com.example.api_petopia.util.LocalDateTimeHelper;
import com.example.api_petopia.util.NumberUtil;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BlogSeeder {
    @Autowired
    TypePetSeeder typePetSeeder;

    @Autowired
    CatePetSeeder catePetSeeder;

    @Autowired
    UserSeeder userSeeder;
    List<SeedByTime> seeder;

    @Autowired
    BlogPetRepository blogPetRepository;

    public void configData() {
        seeder = new ArrayList<>();
        seeder.add(SeedByTime.builder()
                .blogPetStatus(BlogPetStatus.ACTIVE).seedTypeByTime(SeedByTimeType.YEAR).year(2022)
                .build());
    }

    Faker faker = new Faker();

    public static final int NUMBER_OF_BLOG = 50;

    public static List<BlogPet> blogPetList = new ArrayList<>();

    public void generate() {
        configData();
        Set<String> mapBlog = new HashSet<>();
        for (SeedByTime seedByTime : seeder) {
            for (int i = 0; i < NUMBER_OF_BLOG; i++) {
                BlogPet blogPet = new BlogPet();

                int randomTypePet = faker.number().numberBetween(0, typePetSeeder.typePetList.size() - 1);
                TypePet typePet = typePetSeeder.typePetList.get(randomTypePet);
                blogPet.setTypePet(typePet);

                int randomCate = faker.number().numberBetween(0, catePetSeeder.catePetList.size());
                CatePet catePet = catePetSeeder.catePetList.get(randomCate);
                blogPet.setCatePet(catePet);

                int randomUser = faker.number().numberBetween(0, userSeeder.userList.size());
                User user = userSeeder.userList.get(randomUser);
                blogPet.setUser(user);

                blogPet.setName(faker.name().title());
                blogPet.setThumbnail("https://picsum.photos/200/300?random="+i);
                blogPet.setImage("https://picsum.photos/200/300?random="+(i*2));
                blogPet.setDescription(faker.lorem().sentence());
                blogPet.setAddress(faker.address().fullAddress());
                blogPet.setAge(faker.date().birthday().toString());
                blogPet.setBreed(faker.animal().name());
                blogPet.setSex(faker.demographic().sex());
                LocalDateTime createdTime = calculatorCreateTime(seedByTime);
                blogPet.setCreatedAt(createdTime);
                int randomStatus = faker.number().numberBetween(0, 2);
                if (randomStatus == 1) {
                    blogPet.setStatus(BlogPetStatus.ACTIVE);
                } else if (randomStatus == 0) {
                    blogPet.setStatus(BlogPetStatus.DEACTIVE);
                }
                blogPetList.add(blogPet);
            }
        }
        blogPetRepository.saveAll(blogPetList);
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
