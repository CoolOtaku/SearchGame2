package com.example.searchgame2;

public class OBJComments {

    int id;
    int user_id;
    String date;
    String autor;
    String email;
    String text_comments;
    String foto;

    public OBJComments(int id, int user_id, String date, String autor, String email, String text, String foto) {
        this.id = id;
        this.user_id = user_id;
        this.date = date;
        this.autor = autor;
        this.email = email;
        this.text_comments = text;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }

    public String getAutor() {
        return autor;
    }

    public String getText_Comments() {
        return text_comments;
    }

    public String getFoto() {
        return foto;
    }
}
