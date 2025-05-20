package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.BorrowRecord;
import com.example.quanlythuvien.util.BorrowDataManager;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowedDocumentPane extends VBox {

    private final String username;
    private final TabPane tabPane;

    public BorrowedDocumentPane(String username) {
        this.username = username;
        setSpacing(15);
        setPadding(new Insets(20));
        VBox.setVgrow(this, Priority.ALWAYS);

        Label title = new Label("📖 Tài liệu đã mượn / chờ duyệt");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab activeTab = new Tab("📘 Đang mượn", buildScrollPane("Đang mượn"));
        Tab pendingTab = new Tab("⏳ Chờ duyệt", buildScrollPane("Chờ duyệt"));

        tabPane.getTabs().addAll(activeTab, pendingTab);

        getChildren().addAll(title, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }

    private ScrollPane buildScrollPane(String statusFilter) {
        VBox list = new VBox(15);
        list.setPadding(new Insets(10));

        List<BorrowRecord> records = BorrowDataManager.findByUsername(username).stream()
                .filter(r -> r.getStatus().equalsIgnoreCase(statusFilter))
                .collect(Collectors.toList());

        for (BorrowRecord record : records) {
            String statusText = getStatusText(record);
            list.getChildren().add(createCard(record, statusText, list));
        }

        ScrollPane scrollPane = new ScrollPane(list);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;");
        return scrollPane;
    }

    private String getStatusText(BorrowRecord record) {
        String status = record.getStatus();
        if ("Chờ duyệt".equalsIgnoreCase(status)) return "⏳ Chờ duyệt";

        try {
            LocalDate due = LocalDate.parse(record.getDueDate());
            LocalDate now = LocalDate.now();
            if (now.isAfter(due)) return "🔴 Quá hạn";
            if (now.plusDays(3).isAfter(due)) return "🟠 Sắp đến hạn";
            return "✅ Còn hạn";
        } catch (Exception e) {
            return "-";
        }
    }

    private HBox createCard(BorrowRecord record, String statusText, VBox parentList) {
        VBox info = new VBox(5);
        Label statusLabel = new Label("Trạng thái: " + statusText);
        Label dueLabel = new Label("Hạn trả: " + record.getDueDate());

        info.getChildren().addAll(
                new Label("📘 " + record.getDocumentTitle()),
                new Label("Ngày mượn: " + record.getBorrowDate()),
                dueLabel,
                new Label("Số lượng: " + record.getQuantity()),
                statusLabel
        );

        HBox card = new HBox();
        card.setStyle("-fx-border-color: #ccc; -fx-padding: 12; -fx-background-color: #fefefe;");
        card.setAlignment(Pos.CENTER_LEFT);

        if ("Đang mượn".equalsIgnoreCase(record.getStatus())) {
            Button returnBtn = new Button("🔁 Trả tài liệu");
            Button extendBtn = new Button("📆 Gia hạn");

            returnBtn.setOnAction(e -> {
                try {
                    record.setStatus("Đã trả");
                    record.setReturnDate(LocalDate.now().toString());
                    BorrowDataManager.updateRecord(record);

                    returnBtn.setDisable(true);
                    extendBtn.setDisable(true);
                    statusLabel.setText("Trạng thái: ✅ Đã trả");

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(ev -> reloadTabs());
                    pause.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Lỗi khi trả tài liệu.");
                }
            });

            extendBtn.setOnAction(e -> showExtendDialog(record, dueLabel, statusLabel));

            HBox btnBox = new HBox(10, returnBtn, extendBtn);
            VBox content = new VBox(10, info, btnBox);
            content.setAlignment(Pos.CENTER_LEFT);
            card.getChildren().add(content);
        } else {
            VBox content = new VBox(10, info);
            content.setAlignment(Pos.CENTER_LEFT);
            card.getChildren().add(content);
        }

        return card;
    }

    private void showExtendDialog(BorrowRecord record, Label dueLabel, Label statusLabel) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Gia hạn tài liệu");

        Label label = new Label("Nhập số ngày muốn gia hạn:");
        TextField inputField = new TextField();
        inputField.setPromptText("Ví dụ: 5");

        Button confirm = new Button("Xác nhận");
        Label feedback = new Label();

        confirm.setOnAction(e -> {
            try {
                int days = Integer.parseInt(inputField.getText().trim());
                if (days <= 0) {
                    feedback.setText("⚠️ Phải lớn hơn 0.");
                    return;
                }

                LocalDate current = LocalDate.parse(record.getDueDate());
                LocalDate newDue = current.plusDays(days);
                record.setDueDate(newDue.toString());

                BorrowDataManager.updateRecord(record);
                dueLabel.setText("Hạn trả: " + newDue);
                statusLabel.setText("Trạng thái: " + getStatusText(record));
                dialog.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Gia hạn thành công");
                alert.setContentText("Hạn mới: " + newDue);
                alert.show();
            } catch (Exception ex) {
                feedback.setText("❌ Lỗi. Vui lòng kiểm tra lại.");
            }
        });

        VBox layout = new VBox(10, label, inputField, confirm, feedback);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        dialog.setScene(new Scene(layout, 300, 180));
        dialog.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void reloadTabs() {
        tabPane.getTabs().clear();
        tabPane.getTabs().addAll(
                new Tab("📘 Đang mượn", buildScrollPane("Đang mượn")),
                new Tab("⏳ Chờ duyệt", buildScrollPane("Chờ duyệt"))
        );
    }
}
