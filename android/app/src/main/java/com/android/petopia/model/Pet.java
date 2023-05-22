package com.android.petopia.model;

import java.io.Serializable;

public class Pet implements Serializable {
    private Long id;
    private String name;
    private String thumbnail;
    private String image;
    private String description;
    private String address;
    private String age;
    private String breed;
    private String sex;
    private CatePet catePet;
    private TypePet typePet;
    private User user;
    private BlogPetStatus status;

    public Pet() {

    }

    public Pet(Long id, String name, String thumbnail, String image, String description, String address, String age, String breed, String sex, CatePet catePet, TypePet typePet, User user, BlogPetStatus status) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.image = image;
        this.description = description;
        this.address = address;
        this.age = age;
        this.breed = breed;
        this.sex = sex;
        this.catePet = catePet;
        this.typePet = typePet;
        this.user = user;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public CatePet getCatePet() {
        return catePet;
    }

    public void setCatePet(CatePet catePet) {
        this.catePet = catePet;
    }

    public TypePet getTypePet() {
        return typePet;
    }

    public void setTypePet(TypePet typePet) {
        this.typePet = typePet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BlogPetStatus getStatus() {
        return status;
    }

    public void setStatus(BlogPetStatus status) {
        this.status = status;
    }
}
