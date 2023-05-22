package com.example.api_petopia.entity.dto;

import com.example.api_petopia.entity.CatePet;
import com.example.api_petopia.entity.TypePet;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.base.BaseEntity;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogDto{
    private String name;
    @Column(columnDefinition = "text")
    private String thumbnail;
    @Column(columnDefinition = "text")
    private String image;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String address;
    private String age;
    private String breed;
    private String sex;
    private TypePet typePet;
    private CatePet catePet;
    private User user;


}
