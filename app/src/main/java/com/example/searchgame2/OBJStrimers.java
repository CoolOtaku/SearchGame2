package com.example.searchgame2;

public class OBJStrimers {

    String Name;
    String Image;
    int CountViews;
    String language;

    public OBJStrimers(String name, String image, int countViews, String language) {
        Name = name;
        Image = image;
        CountViews = countViews;
        this.language = language;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getCountViews() {
        return CountViews;
    }

    public void setCountViews(int countViews) {
        CountViews = countViews;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
