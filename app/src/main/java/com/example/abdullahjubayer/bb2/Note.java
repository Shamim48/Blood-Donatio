package com.example.abdullahjubayer.bb2;

public class Note {
    private String name;
    private String image;
    private String description;
    private String time;

    public Note() {
        //empty constructor needed
    }

    public Note(String name, String image, String description, String time) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }
}
