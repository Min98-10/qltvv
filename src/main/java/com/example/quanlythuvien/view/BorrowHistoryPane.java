package com.example.quanlythuvien.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BorrowHistoryPane extends VBox {

    public BorrowHistoryPane() {
        setPadding(new Insets(20));
        setSpacing(15);
        VBox.setVgrow(this, Priority.ALWAYS);

        Label title = new Label("📚 Lịch sử mượn trả");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Record> table = new TableView<>();

        TableColumn<Record, String> titleCol = new TableColumn<>("Tài liệu");
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<Record, String> borrowCol = new TableColumn<>("Ngày mượn");
        borrowCol.setCellValueFactory(data -> data.getValue().borrowDateProperty());

        TableColumn<Record, String> returnCol = new TableColumn<>("Ngày trả");
        returnCol.setCellValueFactory(data -> data.getValue().returnDateProperty());

        TableColumn<Record, String> noteCol = new TableColumn<>("Ghi chú");
        noteCol.setCellValueFactory(data -> data.getValue().noteProperty());

        table.getColumns().addAll(titleCol, borrowCol, returnCol, noteCol);
        table.setItems(getSample());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getChildren().addAll(title, table);
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    private ObservableList<Record> getSample() {
        return FXCollections.observableArrayList(
                new Record("Tâm lý học", "01/05/2025", "10/05/2025", "✅ Trả đúng hạn"),
                new Record("Cấu trúc dữ liệu", "15/04/2025", "28/04/2025", "🟠 Trả trễ 1 ngày"),
                new Record("Thiết kế UX", "10/03/2025", "-", "🔴 Quá hạn")
        );
    }

    public static class Record {
        private final javafx.beans.property.SimpleStringProperty title;
        private final javafx.beans.property.SimpleStringProperty borrowDate;
        private final javafx.beans.property.SimpleStringProperty returnDate;
        private final javafx.beans.property.SimpleStringProperty note;

        public Record(String t, String b, String r, String n) {
            title = new javafx.beans.property.SimpleStringProperty(t);
            borrowDate = new javafx.beans.property.SimpleStringProperty(b);
            returnDate = new javafx.beans.property.SimpleStringProperty(r);
            note = new javafx.beans.property.SimpleStringProperty(n);
        }

        public javafx.beans.property.StringProperty titleProperty() {
            return title;
        }

        public javafx.beans.property.StringProperty borrowDateProperty() {
            return borrowDate;
        }

        public javafx.beans.property.StringProperty returnDateProperty() {
            return returnDate;
        }

        public javafx.beans.property.StringProperty noteProperty() {
            return note;
        }
    }
}
