package com.example.project162.Domain;

import com.google.firebase.database.PropertyName;

public class Category {
    private int Id; // Modification ici pour correspondre au nom du champ dans Firebase
    private  String ImagePath;
    private  String Name;

    public Category() {
    }

    @PropertyName("id")
    public int getId() {
        return Id;
    }

    @PropertyName("id")
    public void setId(int id) {
        Id = id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}