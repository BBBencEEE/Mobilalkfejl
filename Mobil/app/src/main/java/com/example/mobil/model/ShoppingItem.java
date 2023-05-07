package com.example.mobil.model;

import com.google.firebase.firestore.DocumentSnapshot;

public class ShoppingItem {
    private String name;
    private String info;
    private String price;
    private float ratedInfo;
    private int imageResource;

    public ShoppingItem() {}

    public ShoppingItem(String name, String info, String price, float ratedInfo, int imageResource) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.ratedInfo = ratedInfo;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public float getRatedInfo() {
        return ratedInfo;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRatedInfo(float ratedInfo) {
        this.ratedInfo = ratedInfo;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public static ShoppingItem fromQueryDocumentSnapshot(DocumentSnapshot snapshot) {
        ShoppingItem item = new ShoppingItem();

        item.setName(snapshot.getString("name"));
        item.setInfo(snapshot.getString("info"));
        item.setPrice(snapshot.getString("price"));
        item.setRatedInfo(Float.parseFloat(snapshot.getDouble("ratedInfo").toString()));
        item.setImageResource(Integer.parseInt(snapshot.getDouble("ratedInfo").toString()));
        return item;
    }
}
