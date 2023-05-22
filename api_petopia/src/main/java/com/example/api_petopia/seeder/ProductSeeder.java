package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.Category;
import com.example.api_petopia.entity.Product;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.LocationStatus;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import com.example.api_petopia.repository.ProductRepository;
import com.example.api_petopia.util.LocalDateTimeHelper;
import com.example.api_petopia.util.NumberUtil;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSeeder {
    @Autowired
    CategorySeeder categorySeeder;

    @Autowired
    ProductRepository productRepository;

    Faker faker = new Faker();


    public static final int NUMBER_OF_PRODUCT = 30;

    public static List<Product> productList = new ArrayList<>();
    List<SeedByTime> seeder;
    public void configData() {
        seeder = new ArrayList<>();
        seeder.add(SeedByTime.builder()
                .seedTypeByTime(SeedByTimeType.YEAR).year(2022)
                .build());
        seeder.add(SeedByTime.builder()
                .seedTypeByTime(SeedByTimeType.YEAR).year(2021).month(Month.DECEMBER)
                .build());
    }

    public void generate() {
        configData();
            for (SeedByTime seedByTime: seeder){
                for (int i = 0; i < NUMBER_OF_PRODUCT; i++) {
                    Product product = new Product();
                    int randomCategories = faker.number().numberBetween(0, categorySeeder.categoryList.size());
                    Category category = categorySeeder.categoryList.get(randomCategories);
                    product.setCategory(category);
                    product.setName(faker.name().title());
                    product.setSold(faker.number().numberBetween(100, 1000));
                    product.setDescription(faker.lorem().sentence());
                    product.setThumbnail("https://picsum.photos/200/300?random="+i);
                    product.setImage("https://picsum.photos/200/300?random="+(i*2));
                    int randomQty = faker.number().numberBetween(10, 500);
                    product.setQty(randomQty);
                    product.setPrice(new BigDecimal(faker.number().numberBetween(10, 200) * 10000));
                    LocalDateTime createdTime = calculatorCreateTime(seedByTime);
                    product.setCreatedAt(createdTime);
                    int randomStatus = faker.number().numberBetween(0, 3);
                    if (randomStatus == 1 ) {
                        product.setStatus(ProductSimpleStatus.ACTIVE);
                    } else if (randomStatus == 0) {
                        product.setStatus(ProductSimpleStatus.DEACTIVE);
                    }
                    else if (randomStatus == 2) {
                        product.setStatus(ProductSimpleStatus.DELETED);
                    }

                    productList.add(product);
                }
            }
        productRepository.saveAll(productList);
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
                tempLocalDateTime = LocalDateTime.of(tempYear, tempMonth, 1, 1, 30, 0);
                result = tempLocalDateTime.plusMonths(1).minusDays(1);
                break;
            case MONTH:
                tempMonth = seedByTime.getMonth().getValue();
                tempYear = seedByTime.getYear();
                tempLocalDateTime = LocalDateTime.of(tempYear, tempMonth, 1, 1, 0, 0);
                LocalDateTime lastDayOfMonth = tempLocalDateTime.plusMonths(1).minusDays(1);
                int randomDay = NumberUtil.getRandomNumber(1, lastDayOfMonth.getDayOfMonth());
                result = LocalDateTime.of(tempYear, tempMonth, randomDay, 2, 0, 0);
                if (result.isAfter(LocalDateTime.now())) {
                    // nếu sau thời gian hiện tại, tức là tháng năm đang thời gian hiện tại
                    randomDay = NumberUtil.getRandomNumber(1, LocalDateTime.now().getDayOfMonth());
                    result = LocalDateTime.of(tempYear, tempMonth, randomDay, 11, 0, 0);
                }
                break;
            case DAY:
                // nếu là ngày thì fix
                result = LocalDateTime.of(seedByTime.getYear(), seedByTime.getMonth(), seedByTime.getDay(), 1, 20, 0);
                break;
        }
        return result;
    }

}
