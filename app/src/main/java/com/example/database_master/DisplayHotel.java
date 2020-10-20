package com.example.database_master;

public class DisplayHotel {
    String name,imagepath;
    float rating;

    public DisplayHotel(String name, String imagepath, float rating) {
        this.name = name;
        this.imagepath = imagepath;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
