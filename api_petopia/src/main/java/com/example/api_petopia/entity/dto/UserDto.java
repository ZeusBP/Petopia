package com.example.api_petopia.entity.dto;

import com.example.api_petopia.entity.Role;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.UserStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private String id;
    @NotEmpty(message = "username missing")
    private String username;
    @NotEmpty(message = "password missing")
    @Min(value = 8, message = "password not strong enough")
    private String password;
    @NotEmpty(message = "password repeat missing")
    private String rePass;
    @NotEmpty(message = "thumbnail missing")
    private String thumbnail;
    @NotEmpty(message = "fullName missing")
    private String fullName;
    @Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "email wrong")
    private String email;
    @NotEmpty(message = "phone missing")
    private String phone;
    @NotEmpty(message = "address missing")
    private String address;
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;
    private Set<Role> roles;

    public UserDto(User save) {
    }
}
