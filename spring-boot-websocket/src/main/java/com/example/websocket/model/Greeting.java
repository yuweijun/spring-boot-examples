package com.example.websocket.model;

/**
 * Created by yuweijun on 2017-07-31.
 */
public class Greeting {

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "content='" + content + '\'' +
                '}';
    }
}
