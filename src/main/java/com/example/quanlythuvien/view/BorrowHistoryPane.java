package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.BorrowRecord;
import com.example.quanlythuvien.util.BorrowDataManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowHistoryPane extends VBox {

    private final TableView<Record> table;

    public BorrowHistoryPane(String username) {
        setPadding(new Insets(20));
        setSpacing(15);
        VBox.setVgrow(this, Priority.ALWAYS);

        Label title = new Label("📚 Lịch sử mượn và đang mượn");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        table = new TableView<>();

        TableColumn<Record, String> titleCol = new TableColumn<>("Tài liệu");
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<Record, String> borrowCol = new TableColumn<>("Ngày mượn");
        borrowCol.setCellValueFactory(data -> data.getValue().borrowDateProperty());

        TableColumn<Record, String> returnCol = new TableColumn<>("Ngày trả");
        returnCol.setCellValueFactory(data -> data.getValue().returnDateProperty());

        TableColumn<Record, String> noteCol = new TableColumn<>("Ghi chú");
        noteCol.setCellValueFactory(data -> data.getValue().noteProperty());

        table.getColumns().addAll(titleCol, borrowCol, returnCol, noteCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        getChildren().addAll(title, table);
        loadData(username);
    }

    public void loadData(String username) {
        table.setItems(loadUserRecords(username));
    }

    private ObservableList<Record> loadUserRecords(String username) {
        List<BorrowRecord> all = BorrowDataManager.findByUsername(username);

        List<Record> records = all.stream()
                .filter(r -> r.getStatus().equalsIgnoreCase("Đã trả") || r.getStatus().equalsIgnoreCase("Đang mượn"))
                .map(r -> {
                    String returnDate = r.getReturnDate() != null ? r.getReturnDate() : (r.getStatus().equalsIgnoreCase("Đang mượn") ? "Chưa trả" : "-");
                    String note;

                    try {
                        LocalDate due = LocalDate.parse(r.getDueDate());
                        if (r.getStatus().equalsIgnoreCase("Đã trả")) {
                            LocalDate returned = LocalDate.parse(r.getReturnDate());
                            if (returned.isAfter(due)) {
                                long daysLate = returned.toEpochDay() - due.toEpochDay();
                                note = " Trả trễ " + daysLate + " ngày";
                            } else {
                                note = "✅ Trả đúng hạn";
                            }
                        } else {
                            LocalDate now = LocalDate.now();
                            if (now.isAfter(due)) {
                                note = " Đang mượn - Quá hạn";
                            } else if (now.plusDays(3).isAfter(due)) {
                                note = " Đang mượn - Sắp đến hạn";
                            } else {
                                note = " Đang mượn";
                            }
                        }
                    } catch (Exception e) {
                        note = "-";
                    }

                    return new Record(r.getDocumentTitle(), r.getBorrowDate(), returnDate, note);
                }).collect(Collectors.toList());

        return FXCollections.observableArrayList(records);
    }

    public static class Record {
        private final SimpleStringProperty title;
        private final SimpleStringProperty borrowDate;
        private final SimpleStringProperty returnDate;
        private final SimpleStringProperty note;

        public Record(String t, String b, String r, String n) {
            title = new SimpleStringProperty(t);
            borrowDate = new SimpleStringProperty(b);
            returnDate = new SimpleStringProperty(r);
            note = new SimpleStringProperty(n);
        }

        public StringProperty titleProperty() { return title; }
        public StringProperty borrowDateProperty() { return borrowDate; }
        public StringProperty returnDateProperty() { return returnDate; }
        public StringProperty noteProperty() { return note; }
    }
}
