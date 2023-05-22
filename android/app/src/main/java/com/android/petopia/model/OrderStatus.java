package com.android.petopia.model;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    PENDING,
    CONFIRMED,
    CANCELLED,
    DONE,
    PROCESSING;
}
