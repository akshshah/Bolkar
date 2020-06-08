package com.example.bolkar.Models;

import java.util.ArrayList;

public class Category {

    private String title;
    private ArrayList<Item> items;

    public Category(String title, ArrayList<Item> items) {
        this.title = title;
        this.items = items;
    }

    public Category() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
