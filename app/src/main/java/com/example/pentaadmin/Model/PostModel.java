package com.example.pentaadmin.Model;

public class PostModel {

    private String photo,title,description;

    public PostModel() {
    }

    public PostModel(String photo, String title, String description) {
        this.photo = photo;
        this.title = title;
        this.description = description;
    }


    public String getphoto() {
        return photo;
    }

    public void setphoto(String photo) {
        this.photo = photo;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }
}
