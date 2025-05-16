package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.BorrowRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BorrowManagementPane extends VBox {

    private final TableView<BorrowRecord> table;
    private final ObservableList<BorrowRecord> data;

    public BorrowManagementPane() {
        setSpacing(15);
        setPadding(new Insets(20));

        Label title = new Label("📚 Quản lý mượn/trả");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        data = FXCollections.observableArrayList(
                new BorrowRecord("khanh01", "Java cơ bản", "01/06/2025", "10/06/2025", "Đã trả"),
                new BorrowRecord("anhnguyen", "Tâm lý học", "20/05/2025", "01/06/2025", "Đã trả"),
                new BorrowRecord("linhvu", "SQL nâng cao", "25/05/2025", "03/06/2025", "Đã trả")
        );

        table = new TableView<>(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-font-size: 14px;");

        TableColumn<BorrowRecord, String> userCol = new TableColumn<>("Người mượn");
        userCol.setCellValueFactory(c -> c.getValue().usernameProperty());

        TableColumn<BorrowRecord, String> docCol = new TableColumn<>("Tài liệu");
        docCol.setCellValueFactory(c -> c.getValue().documentTitleProperty());

        TableColumn<BorrowRecord, String> borrowCol = new TableColumn<>("Ngày mượn");
        borrowCol.setCellValueFactory(c -> c.getValue().borrowDateProperty());

        TableColumn<BorrowRecord, String> returnCol = new TableColumn<>("Hạn trả");
        returnCol.setCellValueFactory(c -> c.getValue().dueDateProperty());

        TableColumn<BorrowRecord, String> statusCol = new TableColumn<>("Trạng thái");
        statusCol.setCellValueFactory(c -> c.getValue().statusProperty());

        TableColumn<BorrowRecord, Void> actionCol = new TableColumn<>("Hành động");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button markReturned = new Button("✅ Trả");
            private final Button extend = new Button("📆 Gia hạn");
            private final HBox box = new HBox(5, markReturned, extend);

            {
                box.setAlignment(Pos.CENTER);
                markReturned.setOnAction(e -> {
                    BorrowRecord rec = getTableView().getItems().get(getIndex());
                    rec.setStatus("Đã trả");
                    table.refresh();
                });

                extend.setOnAction(e -> {
                    BorrowRecord rec = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Gia hạn");
                    alert.setHeaderText("Đã gia hạn thêm 7 ngày cho: " + rec.getDocumentTitle());
                    alert.show();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(box);
            }
        });

        table.getColumns().addAll(userCol, docCol, borrowCol, returnCol, statusCol, actionCol);

        getChildren().addAll(title, table);
        VBox.setVgrow(table, Priority.ALWAYS); // ✅ mở rộng table toàn chiều cao
    }
}
