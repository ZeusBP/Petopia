package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.myenum.OrderStatus;
import lombok.*;

import java.time.Month;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSeedByTime {
    /**
     * Loại gồm 3 kiểu:
     * - Theo ngày (generate chính xác theo ngày tháng năm)
     * - Theo tháng (generate chính xác theo tháng và năm, ngày sinh random)
     * - Theo năm (generate chính xác theo năm, ngày sinh và tháng random)
     */
    private OrderSeedByTimeType seedTypeByTime;
    private int year; // năm nào
    private Month month; // tháng nào
    private int day; // ngày nào
    private int orderCount; // số lượng order trong thời gian này
    private OrderStatus orderStatus; // trạng thái order cần sinh.
}
