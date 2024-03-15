package com.example.myapplication.Helper;

import com.example.myapplication.Models.Product;

import java.util.Comparator;

public class ComparatorPrice {
    public static Comparator<Product> descending = new Comparator<Product>() {
        @Override
        public int compare(Product one, Product two) {
            return - Integer.valueOf(one.getPrice()).compareTo(Integer.valueOf(two.getPrice()));
        }
    };
    public static Comparator<Product> ascending = new Comparator<Product>() {
        @Override
        public int compare(Product one, Product two) {
            return - Integer.valueOf(two.getPrice()).compareTo(Integer.valueOf(one.getPrice()));
        }
    };
}
