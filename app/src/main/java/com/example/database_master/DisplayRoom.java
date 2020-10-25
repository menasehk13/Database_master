package com.example.database_master;

public class DisplayRoom {
    String type, imagepath;
    float number;int price;

    public DisplayRoom(String type, String imagepath, float number, int price) {
        this.type = type;
        this.imagepath = imagepath;
        this.number = number;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}