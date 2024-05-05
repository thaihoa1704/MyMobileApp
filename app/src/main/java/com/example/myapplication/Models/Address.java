package com.example.myapplication.Models;

public class Address {
    private String string;
    private boolean select;

    public Address() {
    }

    public Address(String string, boolean select) {
        this.string = string;
        this.select = select;
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
