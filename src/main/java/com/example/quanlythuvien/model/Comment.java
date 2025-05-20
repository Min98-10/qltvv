package com.example.quanlythuvien.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String documentTitle;
    private final String content;
    private final String date;
    private final int stars;

    public Comment(String username, String documentTitle, String content, String date, int stars) {
        this.username = username;
        this.documentTitle = documentTitle;
        this.content = content;
        this.date = date;
        this.stars = stars;
    }

    public String getUsername() { return username; }

    public String getDocumentTitle() { return documentTitle; }

    public String getContent() { return content; }

    public String getDate() { return date; }

    public int getStars() { return stars; }
}
