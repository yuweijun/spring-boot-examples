package com.example.websocket.model;


public class HelloMessage {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HelloMessage{" +
                "name='" + name + '\'' +
                '}';
    }
}
