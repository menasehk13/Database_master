package com.example.database_master;

public class DisplayRoom {
    String type,imagepath;

    public DisplayRoom(String type, String imagepath, int number) {
        this.type = type;
        this.imagepath = imagepath;
        this.number = number;
    }

    int number;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
