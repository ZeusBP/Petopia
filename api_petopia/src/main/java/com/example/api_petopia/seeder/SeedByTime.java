package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.LocationStatus;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import lombok.*;

import java.time.Month;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedByTime {
    private SeedByTimeType seedTypeByTime;
    private int year; // năm nào
    private Month month; // tháng nào
    private int day; // ngày nào
    private ProductSimpleStatus productSimpleStatus;
    private BlogPetStatus blogPetStatus;
    private LocationStatus locationStatus;
    private UserSeeder userSeeder;
}
