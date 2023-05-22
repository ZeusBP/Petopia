package com.android.petopia.model;

import com.google.gson.annotations.SerializedName;

public class PostRegister {
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("rePass")
    private String rePass;

    public PostRegister(String email, String username, String password, String rePass) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.rePass = rePass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePass() {
        return rePass;
    }

    public void setRePass(String rePass) {
        this.rePass = rePass;
    }
}
