package com.android.petopia.model;

public class UpdateProfileData {
    private String thumbnailAvt;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public String getThumbnailAvt() {
        return thumbnailAvt;
    }

    public void setThumbnailAvt(String thumbnailAvt) {
        this.thumbnailAvt = thumbnailAvt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}