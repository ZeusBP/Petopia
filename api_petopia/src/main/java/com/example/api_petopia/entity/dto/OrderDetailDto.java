package com.example.api_petopia.entity.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDto {
    private Long productId;
    private Long orderId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
}
