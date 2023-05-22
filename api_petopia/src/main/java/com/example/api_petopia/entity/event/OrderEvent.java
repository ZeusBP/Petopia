package com.example.api_petopia.entity.event;

import com.example.api_petopia.entity.dto.OrderDetailDto;
import com.example.api_petopia.entity.myenum.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private Set<OrderDetailDto> orderDetailDTOSet = new HashSet<>();
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
}
