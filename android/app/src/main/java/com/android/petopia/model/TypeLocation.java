package com.android.petopia.model;

import java.io.Serializable;

public class TypeLocation implements Serializable {
    private int id;
    private String name;

    public TypeLocation(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TypeLocation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}