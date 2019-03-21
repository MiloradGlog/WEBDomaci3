package com.company;

public class Quote {

    private String text;
    private String author;

    public Quote(String text, String author){
        this.author = author;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }
}
