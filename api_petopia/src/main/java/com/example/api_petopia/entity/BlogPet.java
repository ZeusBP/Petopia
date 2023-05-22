package com.example.api_petopia.entity;

import com.example.api_petopia.entity.base.BaseEntity;
import com.example.api_petopia.entity.myenum.BlogPetStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.xml.soap.Name;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "pets")
public class BlogPet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "typepet_id")
    private TypePet typePet;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "catpet_id")
    private CatePet catePet;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private BlogPetStatus status;
}
