package com.example.api_petopia.entity;

import com.example.api_petopia.entity.base.BaseEntity;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int sold;

    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String thumbnail;
    @Column(columnDefinition = "text")
    private String image;
    private int qty;
    private BigDecimal price;

    @Enumerated(EnumType.ORDINAL)
    private ProductSimpleStatus status;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")

    private Category category;

}
