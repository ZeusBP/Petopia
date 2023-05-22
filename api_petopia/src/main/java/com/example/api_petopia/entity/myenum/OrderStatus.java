package com.example.api_petopia.entity.myenum;

import java.util.Random;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    DONE,
    PROCESSING;
    private static final Random PRNG = new Random();

    public static OrderStatus randomDirection()  {
        OrderStatus[] statuses = values();
        return statuses[PRNG.nextInt(statuses.length)];
    }
}
