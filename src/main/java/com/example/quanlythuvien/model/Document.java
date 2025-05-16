package com.example.quanlythuvien.model;

public class Document {
    private int id;
    private String title;
    private String author;
    private String imageUrl;
    private String category;
    private String status;
    private int viewCount;
    private String updatedAt;
    private String summary;

    // ✅ Constructor đầy đủ
    public Document(String title, String author, String imageUrl,
                    String category, String status, int viewCount,
                    String updatedAt, String summary) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.category = category;
        this.status = status;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
        this.summary = summary;
    }

    // ✅ Constructor có id (từ DB) - dùng trong DAO
    public Document(int id, String title, String author, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.category = "";
        this.status = "";
        this.viewCount = 0;
        this.updatedAt = "";
        this.summary = "";
    }

    // ===== Getter =====
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public int getViewCount() {
        return viewCount;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getSummary() {
        return summary;
    }
}
