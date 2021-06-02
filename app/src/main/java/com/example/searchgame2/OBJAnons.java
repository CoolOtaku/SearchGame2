package com.example.searchgame2;

public class OBJAnons {

    String anons_title;
    String anons_poster;
    String anons_reliz_date;
    String anons_platform;

    public OBJAnons(String anons_title, String anons_poster, String anons_reliz_date, String anons_platform) {
        this.anons_title=anons_title;
        this.anons_poster = anons_poster;
        this.anons_reliz_date = anons_reliz_date;
        this.anons_platform = anons_platform;
    }

    public String getAnons_title() {
        return anons_title;
    }

    public String getAnons_poster() {
        return anons_poster;
    }

    public String getAnons_reliz_date() {
        return anons_reliz_date;
    }

    public String getAnons_platform() {
        return anons_platform;
    }

}
