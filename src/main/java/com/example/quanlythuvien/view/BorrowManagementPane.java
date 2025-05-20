package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.BorrowRecord;
import com.example.quanlythuvien.util.BorrowDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowManagementPane extends VBox {

    private final TableView<BorrowRecord> table;
    private final ObservableList<BorrowRecord> data;

    public BorrowManagementPane() {
        setSpacing(15);
        setPadding(new Insets(20));

        Label title = new Label("📚 Quản lý mượn/trả");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Lọc bỏ bản ghi "Chờ duyệt" và "Từ chối"
        List<BorrowRecord> records = BorrowDataManager.load().stream()
                .filter(r -> {
                    String status = r.getStatus().toLowerCase();
                    return !status.equals("chờ duyệt") && !status.equals("từ chối");
                })
                .collect(Collectors.toList());

        data = FXCollections.observableArrayList(records);

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

        TableColumn<BorrowRecord, Integer> qtyCol = new TableColumn<>("Số lượng");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
                    rec.setReturnDate(LocalDate.now().toString());
                    BorrowDataManager.updateRecord(rec);
                    table.refresh();
                });

                extend.setOnAction(e -> {
                    BorrowRecord rec = getTableView().getItems().get(getIndex());
                    showExtensionDialog(rec);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    BorrowRecord rec = getTableView().getItems().get(getIndex());
                    boolean isReturned = rec.getStatus().equalsIgnoreCase("Đã trả");

                    markReturned.setDisable(isReturned);
                    extend.setDisable(isReturned);

                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(userCol, docCol, borrowCol, returnCol, statusCol, qtyCol, actionCol);

        getChildren().addAll(title, table);
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    private void showExtensionDialog(BorrowRecord record) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Gia hạn tài liệu");

        Label label = new Label("Nhập số ngày muốn gia hạn:");
        TextField daysField = new TextField();
        daysField.setPromptText("Ví dụ: 5");

        Button confirm = new Button("Xác nhận");
        confirm.setDefaultButton(true);

        Label feedback = new Label();

        confirm.setOnAction(e -> {
            try {
                int days = Integer.parseInt(daysField.getText().trim());
                if (days <= 0) {
                    feedback.setText("⚠️ Số ngày phải lớn hơn 0.");
                    return;
                }

                LocalDate newDue = LocalDate.parse(record.getDueDate()).plusDays(days);
                record.setDueDate(newDue.toString());

                BorrowDataManager.updateRecord(record);
                table.refresh();

                dialog.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Gia hạn thành công");
                alert.setHeaderText("Đã gia hạn " + days + " ngày cho: " + record.getDocumentTitle());
                alert.setContentText("Hạn trả mới: " + newDue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                alert.show();

            } catch (NumberFormatException ex) {
                feedback.setText("⚠️ Vui lòng nhập số nguyên hợp lệ.");
            } catch (Exception ex) {
                feedback.setText("❌ Lỗi khi gia hạn.");
            }
        });

        VBox layout = new VBox(10, label, daysField, confirm, feedback);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        dialog.setScene(new Scene(layout, 300, 180));
        dialog.showAndWait();
    }
}
