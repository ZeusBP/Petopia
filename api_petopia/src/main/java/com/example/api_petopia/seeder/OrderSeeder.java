package com.example.api_petopia.seeder;

import com.example.api_petopia.entity.*;
import com.example.api_petopia.entity.myenum.OrderStatus;
import com.example.api_petopia.repository.OrderDetailRepository;
import com.example.api_petopia.repository.OrderRepository;
import com.example.api_petopia.util.DateTimeHelper;
import com.example.api_petopia.util.NumberUtil;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Component
public class OrderSeeder {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    public static List<Order> orders;
    public static final int NUMBER_OF_ORDER = 1000;
    public static final int NUMBER_OF_DONE = 600;
    public static final int NUMBER_OF_CANCEL = 200;
    public static final int NUMBER_OF_PENDING = 200;
    public static final int MIN_ORDER_DETAIL = 2;
    public static final int MAX_ORDER_DETAIL = 5;
    public static final int MIN_PRODUCT_QUANTITY = 1;
    public static final int MAX_PRODUCT_QUANTITY = 5;

    @Autowired
    OrderRepository orderRepository;
    List<OrderSeedByTime> seeder;

    public void configData() {
        seeder = new ArrayList<>();
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.DONE).seedTypeByTime(OrderSeedByTimeType.DAY).day(18).month(Month.JUNE).year(2022).orderCount(50)
                        .build());
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.DONE).seedTypeByTime(OrderSeedByTimeType.DAY).day(17).month(Month.JUNE).year(2022).orderCount(50)
                        .build());
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.PENDING).seedTypeByTime(OrderSeedByTimeType.MONTH).month(Month.JUNE).year(2022).orderCount(10)
                        .build());
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.PENDING).seedTypeByTime(OrderSeedByTimeType.MONTH).month(Month.JUNE).year(2022).orderCount(10)
                        .build());
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.CONFIRMED).seedTypeByTime(OrderSeedByTimeType.MONTH).month(Month.MAY).year(2022).orderCount(10)
                        .build());
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.CONFIRMED).seedTypeByTime(OrderSeedByTimeType.MONTH).month(Month.APRIL).year(2022).orderCount(10)
                        .build());
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.CANCELLED).seedTypeByTime(OrderSeedByTimeType.MONTH).month(Month.APRIL).year(2022).orderCount(4)
                        .build());
        seeder.add(
                OrderSeedByTime.builder()
                        .orderStatus(OrderStatus.CANCELLED).seedTypeByTime(OrderSeedByTimeType.MONTH).month(Month.MARCH).year(2022).orderCount(10)
                        .build());
    }

    public void generate() {
        configData();
        Faker faker = new Faker();
        orders = new ArrayList<>();
        for (OrderSeedByTime orderSeedByTime :
                seeder) {
            int numberOfOrder = orderSeedByTime.getOrderCount();
            for (int i = 0; i < numberOfOrder; i++) {
                // lấy random user.
                int randomUserIndex = NumberUtil.getRandomNumber(0, UserSeeder.userList.size() - 1);
                User user = UserSeeder.userList.get(randomUserIndex);
                // Tạo mới đơn hàng.
                Order order = new Order();
                order.setStatus(orderSeedByTime.getOrderStatus());
                LocalDateTime orderCreatedTime = calculateOrderCreatedTime(orderSeedByTime);
                order.setCreatedAt(orderCreatedTime);
                order.setUpdatedAt(orderCreatedTime);
                order.setUser(user);
                order.setName(faker.name().fullName());
                order.setAddress(faker.address().fullAddress());
                // Tạo danh sách order detail cho đơn hàng.
                Set<OrderDetail> orderDetails = new HashSet<>();
                // map này dùng để check sự tồn tại của sản phẩm trong order detail.
                HashMap<Long, Product> mapProduct = new HashMap<>();
                // generate số lượng của order detail.
                int orderDetailNumber = NumberUtil.getRandomNumber(MIN_ORDER_DETAIL, MAX_ORDER_DETAIL);
                for (int j = 0; j < orderDetailNumber; j++) {
                    // lấy random product.
                    int randomProductIndex = NumberUtil.getRandomNumber(0, ProductSeeder.productList.size() - 1);
                    Product product = ProductSeeder.productList.get(randomProductIndex);
                    // check tồn tại
                    if (mapProduct.containsKey(product.getId())) {
                        continue; // bỏ qua hoặc cộng dồn
                    }
                    // tạo order detail theo sản phẩm random
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(new OrderDetailId(order.getId(), product.getId()));
                    orderDetail.setOrder(order); // set quan hệ
                    orderDetail.setProduct(product); // set quan hệ
                    orderDetail.setUnitPrice(product.getPrice()); // giá theo sản phẩm
                    // random số lượng theo cấu hình
                    orderDetail.setQuantity(NumberUtil.getRandomNumber(MIN_PRODUCT_QUANTITY, MAX_PRODUCT_QUANTITY));
                    // đưa vào danh sách order detail
                    orderDetails.add(orderDetail);
                    mapProduct.put(product.getId(), product);
                }
                // set quan hệ với order
                order.setOrderDetails(orderDetails);
                order.calculateTotalPrice();
                order.setShoppingCart(faker.bool().bool());
                orders.add(order);
            }
        }
        orderRepository.saveAll(orders);
    }

    public void generateOrderDetail() {
        for (Order order :
                orders) {
            order.setUpdatedAt(LocalDateTime.now());
            orderDetailRepository.saveAll(order.getOrderDetails());
        }
    }

    private LocalDateTime calculateOrderCreatedTime(OrderSeedByTime orderSeedByTime) {
        LocalDateTime result = null;
        LocalDateTime tempLocalDateTime = null;
        int tempMonth = 1;
        int tempYear = 2022;
        switch (orderSeedByTime.getSeedTypeByTime()) {
            case YEAR:
                // nếu theo năm thì random tháng và ngày.
                tempMonth = DateTimeHelper.getRandomMonth().getValue();
                tempYear = orderSeedByTime.getYear();
                tempLocalDateTime = LocalDateTime.of(tempYear, tempMonth, 1, 0, 0, 0);
                result = tempLocalDateTime.plusMonths(1).minusDays(1);
                break;
            case MONTH:
                // nếu theo tháng, năm thì random ngày.
                tempMonth = orderSeedByTime.getMonth().getValue();
                tempYear = orderSeedByTime.getYear();
                tempLocalDateTime = LocalDateTime.of(tempYear, tempMonth, 1, 0, 0, 0);
                LocalDateTime lastDayOfMonth = tempLocalDateTime.plusMonths(1).minusDays(1);
                int randomDay = NumberUtil.getRandomNumber(1, lastDayOfMonth.getDayOfMonth());
                result = LocalDateTime.of(tempYear, tempMonth, randomDay, 0, 0, 0);
                if (result.isAfter(LocalDateTime.now())) {
                    // nếu sau thời gian hiện tại, tức là tháng năm đang thời gian hiện tại
                    randomDay = NumberUtil.getRandomNumber(1, LocalDateTime.now().getDayOfMonth());
                    result = LocalDateTime.of(tempYear, tempMonth, randomDay, 0, 0, 0);
                }
                break;
            case DAY:
                // nếu là ngày thì fix
                result = LocalDateTime.of(orderSeedByTime.getYear(), orderSeedByTime.getMonth(), orderSeedByTime.getDay(), 0, 0, 0);
                break;
        }
        return result;
    }
}

