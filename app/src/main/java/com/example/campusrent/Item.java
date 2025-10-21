package com.example.campusrent;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Item {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String userId;
    private Date timestamp;

    public Item() {
        // Needed for Firestore
    }

    public Item(String name, String description, double price, String imageUrl, String userId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    @ServerTimestamp
    public Date getTimestamp() {
        return timestamp;
    }
}