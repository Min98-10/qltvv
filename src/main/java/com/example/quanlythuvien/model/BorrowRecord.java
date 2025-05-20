package com.example.quanlythuvien.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.UUID;

public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    // ID định danh duy nhất mỗi bản ghi
    private String id;

    // transient JavaFX properties (không lưu khi serialize)
    private transient StringProperty username;
    private transient StringProperty documentTitle;
    private transient StringProperty borrowDate;
    private transient StringProperty dueDate;
    private transient StringProperty status;
    private transient StringProperty returnDate;
    private transient SimpleIntegerProperty quantity;

    // Dữ liệu gốc được lưu vào file
    private final String _username;
    private final String _documentTitle;
    private final String _borrowDate;
    private String _dueDate;
    private String _status;
    private String _returnDate;
    private int _quantity;

    // === Constructor mặc định ===
    public BorrowRecord(String username, String documentTitle, String borrowDate, String dueDate, String status, int quantity) {
        this(UUID.randomUUID().toString(), username, documentTitle, borrowDate, dueDate, status, null, quantity);
    }

    public BorrowRecord(String id, String username, String documentTitle, String borrowDate, String dueDate, String status, String returnDate, int quantity) {
        this.id = (id != null && !id.isEmpty()) ? id : UUID.randomUUID().toString();
        this._username = username;
        this._documentTitle = documentTitle;
        this._borrowDate = borrowDate;
        this._dueDate = dueDate;
        this._status = status;
        this._returnDate = returnDate;
        this._quantity = quantity;

        ensurePropertiesInitialized();
    }

    // === JavaFX Property getters for TableView ===
    public StringProperty usernameProperty() { ensurePropertiesInitialized(); return username; }
    public StringProperty documentTitleProperty() { ensurePropertiesInitialized(); return documentTitle; }
    public StringProperty borrowDateProperty() { ensurePropertiesInitialized(); return borrowDate; }
    public StringProperty dueDateProperty() { ensurePropertiesInitialized(); return dueDate; }
    public StringProperty statusProperty() { ensurePropertiesInitialized(); return status; }
    public StringProperty returnDateProperty() { ensurePropertiesInitialized(); return returnDate; }
    public SimpleIntegerProperty quantityProperty() { ensurePropertiesInitialized(); return quantity; }

    // === Getters ===
    public String getId() { return id; }
    public String getUsername() { return _username; }
    public String getDocumentTitle() { return _documentTitle; }
    public String getBorrowDate() { return _borrowDate; }
    public String getDueDate() { return _dueDate; }
    public String getStatus() { return _status; }
    public String getReturnDate() { return _returnDate; }
    public int getQuantity() { return _quantity; }

    // === Setters ===
    public void setId(String id) { this.id = id; }

    public void setDueDate(String newDate) {
        this._dueDate = newDate;
        if (dueDate != null) dueDate.set(newDate);
    }

    public void setStatus(String newStatus) {
        this._status = newStatus;
        if (status != null) status.set(newStatus);
    }

    public void setReturnDate(String returnDate) {
        this._returnDate = returnDate;
        if (this.returnDate != null) this.returnDate.set(returnDate);
    }

    public void setQuantity(int newQuantity) {
        this._quantity = newQuantity;
        if (quantity != null) quantity.set(newQuantity);
    }

    // === Bảo đảm khởi tạo Property nếu deserialize từ file cũ ===
    private void ensurePropertiesInitialized() {
        if (username == null) username = new SimpleStringProperty(_username);
        if (documentTitle == null) documentTitle = new SimpleStringProperty(_documentTitle);
        if (borrowDate == null) borrowDate = new SimpleStringProperty(_borrowDate);
        if (dueDate == null) dueDate = new SimpleStringProperty(_dueDate);
        if (status == null) status = new SimpleStringProperty(_status != null ? _status : "-");
        if (returnDate == null) returnDate = new SimpleStringProperty(_returnDate != null ? _returnDate : "-");
        if (quantity == null) quantity = new SimpleIntegerProperty(_quantity);
    }
}
