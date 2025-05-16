package com.example.quanlythuvien.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BorrowRecord {
    private final StringProperty username;
    private final StringProperty documentTitle;
    private final StringProperty borrowDate;
    private final StringProperty dueDate;
    private final StringProperty status;

    public BorrowRecord(String username, String documentTitle, String borrowDate, String dueDate, String status) {
        this.username = new SimpleStringProperty(username);
        this.documentTitle = new SimpleStringProperty(documentTitle);
        this.borrowDate = new SimpleStringProperty(borrowDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty documentTitleProperty() {
        return documentTitle;
    }

    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getDocumentTitle() {
        return documentTitle.get();
    }

    public void setStatus(String newStatus) {
        status.set(newStatus);
    }
}
