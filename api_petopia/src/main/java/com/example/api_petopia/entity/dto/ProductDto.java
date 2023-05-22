package com.example.api_petopia.entity.dto;

import com.example.api_petopia.entity.Category;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDto {
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

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")

    private Category category;

    @Enumerated(EnumType.ORDINAL)
    private ProductSimpleStatus status;
}
