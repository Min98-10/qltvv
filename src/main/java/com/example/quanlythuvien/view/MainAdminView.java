package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainAdminView {

    public void show(Stage stage) {
        // ===== HEADER =====
        Label logo = new Label("📚 Thư viện");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Tìm kiếm tài liệu...");
        searchField.setStyle("-fx-font-size: 14px;");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Label userLabel = new Label("Admin");
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
        VBox.setVgrow(centerBox, Priority.ALWAYS); // ✅ Cho phép centerBox giãn theo chiều cao

        String[] functions = {
                "Trang chủ",
                "Quản lý tài liệu",
                "Quản lý thành viên",
                "Quản lý mượn/trả",
                "Thống kê"
        };

        for (String name : functions) {
            Button btn = new Button(name);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefHeight(60);
            btn.setStyle("-fx-font-size: 14px; -fx-alignment: center;");
            VBox.setVgrow(btn, Priority.ALWAYS);

            switch (name) {
                case "Quản lý tài liệu":
                    btn.setOnAction(_ -> {
                        DocumentListPane listPane = new DocumentListPane(doc -> {
                            DocumentDetailPane detailPane = new DocumentDetailPane();
                            detailPane.setData(doc);
                            centerBox.getChildren().setAll(detailPane);
                            VBox.setVgrow(detailPane, Priority.ALWAYS);
                        });
                        VBox.setVgrow(listPane, Priority.ALWAYS); // ✅
                        centerBox.getChildren().setAll(listPane);
                    });
                    break;

                case "Quản lý thành viên":
                    MemberManagementPane memberPane = new MemberManagementPane();
                    VBox.setVgrow(memberPane, Priority.ALWAYS); // ✅
                    btn.setOnAction(_ -> centerBox.getChildren().setAll(memberPane));
                    break;

                case "Quản lý mượn/trả":
                    BorrowManagementPane borrowPane = new BorrowManagementPane();
                    VBox.setVgrow(borrowPane, Priority.ALWAYS); // ✅ QUAN TRỌNG
                    btn.setOnAction(_ -> centerBox.getChildren().setAll(borrowPane));
                    break;

                case "Thống kê":
                    StatisticsPane statsPane = new StatisticsPane();
                    VBox.setVgrow(statsPane, Priority.ALWAYS);
                    btn.setOnAction(_ -> centerBox.getChildren().setAll(statsPane));
                    break;


                default:
                    btn.setOnAction(_ -> centerBox.getChildren().setAll(new Label("Bạn đã chọn: " + name)));
                    break;
            }

            sidebar.getChildren().add(btn);
        }

        centerBox.getChildren().add(new Label("Chọn chức năng để hiển thị nội dung..."));

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Thư viện - Admin");
        stage.setScene(scene);
        stage.show();
    }

}
