package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainUserView {
    private final String username;

    public MainUserView(String username) {
        this.username = username;
    }

    public void show(Stage stage) {
        // ===== HEADER =====
        Label logo = new Label("📚 Thư viện");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Tìm kiếm tài liệu...");
        searchField.setStyle("-fx-font-size: 14px;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Label userLabel = new Label(username);
        Button logoutBtn = new Button("Đăng xuất");
        logoutBtn.setOnAction(_ -> new LoginView().show(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(15, logo, searchField, spacer, userLabel, logoutBtn);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #f9f9f9;");

        // ===== SIDEBAR =====
        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #f1f1f1;");
        sidebar.setPrefWidth(180);

        VBox centerBox = new VBox();
        centerBox.setPadding(new Insets(15));
        centerBox.setSpacing(10);
        VBox.setVgrow(centerBox, Priority.ALWAYS); // ✅ cho phép kéo giãn trung tâm

        String[] functions = {
                "Trang chủ", "Tài liệu có sẵn", "Tài liệu đã mượn",
                "Lịch sử mượn trả", "Thông tin tài khoản"
        };

        for (String name : functions) {
            Button btn = new Button(name);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefHeight(60);
            btn.setStyle("-fx-font-size: 14px; -fx-alignment: center;");
            VBox.setVgrow(btn, Priority.ALWAYS);

            if (name.equals("Tài liệu có sẵn")) {
                btn.setOnAction(e -> {
                    DocumentListPaneUser list = new DocumentListPaneUser(doc -> {
                        DocumentDetailPaneUser detail = new DocumentDetailPaneUser();
                        detail.setData(doc);
                        VBox.setVgrow(detail, Priority.ALWAYS);
                        centerBox.getChildren().setAll(detail);
                    });
                    VBox.setVgrow(list, Priority.ALWAYS); // ✅
                    centerBox.getChildren().setAll(list);
                });
            } else if (name.equals("Tài liệu đã mượn")) {
                BorrowedDocumentPane borrowed = new BorrowedDocumentPane();
                VBox.setVgrow(borrowed, Priority.ALWAYS);
                btn.setOnAction(e -> centerBox.getChildren().setAll(borrowed));
            } else if (name.equals("Lịch sử mượn trả")) {
                BorrowHistoryPane history = new BorrowHistoryPane();
                VBox.setVgrow(history, Priority.ALWAYS);
                btn.setOnAction(e -> centerBox.getChildren().setAll(history));
            } else if (name.equals("Thông tin tài khoản")) {
                UserProfilePane profile = new UserProfilePane(username);
                VBox.setVgrow(profile, Priority.ALWAYS);
                btn.setOnAction(e -> centerBox.getChildren().setAll(profile));
            } else {
                btn.setOnAction(e -> centerBox.getChildren().setAll(new Label("Bạn đã chọn: " + name)));
            }

            sidebar.getChildren().add(btn);
        }

        centerBox.getChildren().add(new Label("Chọn chức năng để bắt đầu"));

        // ===== RIGHT =====
        VBox rightBox = new VBox(20);
        rightBox.setPadding(new Insets(10));
        rightBox.setPrefWidth(220);

        VBox ratingBox = new VBox(5);
        ratingBox.setStyle("-fx-border-color: #ddd; -fx-padding: 10;");
        ratingBox.getChildren().addAll(
                new Label("⭐ Đánh giá nổi bật"),
                new Label("Giáo trình Java ★★★★★"),
                new Label("Tâm lý học ★★★★☆")
        );

        VBox borrowedBox = new VBox(5);
        borrowedBox.setStyle("-fx-border-color: #ddd; -fx-padding: 10;");
        borrowedBox.getChildren().addAll(
                new Label("📖 Tài liệu đã mượn"),
                new Label("Sức bền vật liệu - 01/04/2024"),
                new Label("Kỹ năng mềm - 10/03/2024")
        );

        rightBox.getChildren().addAll(ratingBox, borrowedBox);

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(centerBox);
        root.setRight(rightBox);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Thư viện - Người dùng");
        stage.setScene(scene);
        stage.show();
    }
}
