package com.example.myapplication.Models;

public class Address{
    private String id;
    private String string;
    private boolean select;

    public Address() {
    }

    public Address(String id, String string, boolean select) {
        this.id = id;
        this.string = string;
        this.select = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
