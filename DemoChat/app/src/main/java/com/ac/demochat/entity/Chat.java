package com.ac.demochat.entity;

public class Chat {

    private String message;
    private String uid;
    private String name;


    public Chat() {
    }

    public Chat(String message, String uid, String name) {
        this.message = message;
        this.uid = uid;
        this.name = name;
    }


    public String getMessage() {
        return message;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

}
