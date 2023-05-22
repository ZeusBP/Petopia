package com.android.petopia.data_local;

import android.content.Context;

import com.android.petopia.model.User;
import com.google.gson.Gson;

public class DataLocalManager {
    private static final String TOKEN = "ACCESS_TOKEN";
    private static final String USER = "USER";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance() {
        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setToken(String token) {
        DataLocalManager.getInstance().mySharedPreferences.putToken(TOKEN, token);
    }

    public static String getToken(){
        return DataLocalManager.getInstance().mySharedPreferences.getToken(TOKEN);
    }
    public static void deleteToken(){
        DataLocalManager.getInstance().mySharedPreferences.deleteToken(TOKEN);
    }

    public static void setUser(User userData) {
        Gson gson = new Gson();
        String user = gson.toJson(userData);
        DataLocalManager.getInstance().mySharedPreferences.putUser(USER, user);
    }

    public static User getUser(){
        String user = DataLocalManager.getInstance().mySharedPreferences.getUser(USER);
        Gson gson = new Gson();
        User userData = gson.fromJson(user, User.class);
        return userData;
    }
    public static void deleteUser(){
        DataLocalManager.getInstance().mySharedPreferences.deleteUser(USER);
    }
}