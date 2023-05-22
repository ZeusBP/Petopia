package com.example.api_petopia.entity;

import com.example.api_petopia.entity.base.BaseEntity;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String thumbnailAvt;
    private String fullName;
    private String email;
    private String phone;
    @Column(columnDefinition = "text")
    private String address;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

}
