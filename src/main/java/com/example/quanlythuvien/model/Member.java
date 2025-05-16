package com.example.quanlythuvien.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {
    private final StringProperty username;
    private final StringProperty fullName;
    private final StringProperty birthDate;
    private final StringProperty id;
    private final StringProperty email;
    private final StringProperty address;

    public Member(String username, String fullName, String birthDate, String id, String email, String address) {
        this.username = new SimpleStringProperty(username);
        this.fullName = new SimpleStringProperty(fullName);
        this.birthDate = new SimpleStringProperty(birthDate);
        this.id = new SimpleStringProperty(id);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty birthDateProperty() {
        return birthDate;
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getUsername() {
        return username.get();
    }

    public String getFullName() {
        return fullName.get();
    }
}
