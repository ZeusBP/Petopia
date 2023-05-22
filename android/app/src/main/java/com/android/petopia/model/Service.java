package com.android.petopia.model;

import java.io.Serializable;

public class Service implements Serializable {
    private Long id;
    private String nameLocation;
    private String thumbnail;
    private String address;
    private String phone;
    private String email;
    private String description;
    private TypeLocation typeLocation;
    private User user;
    private LocationStatus status;

    public Service() {
    }

    public Service(Long id, String nameLocation, String thumbnail, String address, String phone, String email, String description, TypeLocation typeLocation, User user, LocationStatus status) {
        this.id = id;
        this.nameLocation = nameLocation;
        this.thumbnail = thumbnail;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.typeLocation = typeLocation;
        this.user = user;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameLocation() {
        return nameLocation;
    }

    public void setNameLocation(String nameLocation) {
        this.nameLocation = nameLocation;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeLocation getTypeLocation() {
        return typeLocation;
    }

    public void setTypeLocation(TypeLocation typeLocation) {
        this.typeLocation = typeLocation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocationStatus getStatus() {
        return status;
    }

    public void setStatus(LocationStatus status) {
        this.status = status;
    }
}