package com.example.fire;

public class User {

    private String uid, name , id , profileimage;


    public User() {

    }

    public User(String uid, String name, String id, String profileimage) {
        this.uid = uid;
        this.name = name;
        this.id = id;
        this.profileimage = profileimage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

}
