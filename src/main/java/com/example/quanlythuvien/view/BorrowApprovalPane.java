package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.BorrowRecord;
import com.example.quanlythuvien.util.BorrowDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class BorrowApprovalPane extends VBox {

    private final TableView<BorrowRecord> table;
    private final ObservableList<BorrowRecord> data;

    public BorrowApprovalPane() {
        setPadding(new Insets(20));
        setSpacing(15);
        VBox.setVgrow(this, Priority.ALWAYS);

        Label title = new Label("📥 Yêu cầu mượn đang chờ duyệt");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<BorrowRecord> pendingList = BorrowDataManager.load().stream()
                .filter(r -> r.getStatus().equalsIgnoreCase("Chờ duyệt"))
                .toList();

        data = FXCollections.observableArrayList(pendingList);
        table.setItems(data);

        TableColumn<BorrowRecord, String> userCol = new TableColumn<>("Người mượn");
        userCol.setCellValueFactory(r -> r.getValue().usernameProperty());

        TableColumn<BorrowRecord, String> docCol = new TableColumn<>("Tài liệu");
        docCol.setCellValueFactory(r -> r.getValue().documentTitleProperty());

        TableColumn<BorrowRecord, String> borrowDateCol = new TableColumn<>("Ngày mượn");
        borrowDateCol.setCellValueFactory(r -> r.getValue().borrowDateProperty());

        TableColumn<BorrowRecord, String> dueDateCol = new TableColumn<>("Hạn trả");
        dueDateCol.setCellValueFactory(r -> r.getValue().dueDateProperty());

        TableColumn<BorrowRecord, Void> actionCol = new TableColumn<>("Hành động");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button approveBtn = new Button("✅ Đồng ý");
            private final Button rejectBtn = new Button("❌ Từ chối");
            private final HBox hbox = new HBox(10, approveBtn, rejectBtn);

            {
                hbox.setAlignment(Pos.CENTER);

                approveBtn.setOnAction(e -> {
                    BorrowRecord record = getTableView().getItems().get(getIndex());
                    record.setStatus("Đang mượn");
                    BorrowDataManager.updateRecord(record);
                    data.remove(record);
                    showAlert("Đã đồng ý cho mượn tài liệu.");
                });

                rejectBtn.setOnAction(e -> {
                    BorrowRecord record = getTableView().getItems().get(getIndex());
                    record.setStatus("Từ chối");
                    BorrowDataManager.updateRecord(record);
                    data.remove(record);
                    showAlert("Yêu cầu mượn đã bị từ chối.");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        table.getColumns().addAll(userCol, docCol, borrowDateCol, dueDateCol, actionCol);

        getChildren().addAll(title, table);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
