package com.example.quanlythuvien.view;

import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatisticsPane extends VBox {

    public StatisticsPane() {
        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_LEFT);

        Label title = new Label("📊 Thống kê thư viện");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label totalDocs = new Label("📚 Tổng tài liệu: 152");
        Label totalUsers = new Label("👥 Tổng thành viên: 38");
        Label totalBorrows = new Label("📖 Tổng lượt mượn: 419");
        Label mostPopular = new Label("🔥 Tài liệu được mượn nhiều nhất: Java cơ bản (82 lượt)");

        totalDocs.setStyle("-fx-font-size: 15px;");
        totalUsers.setStyle("-fx-font-size: 15px;");
        totalBorrows.setStyle("-fx-font-size: 15px;");
        mostPopular.setStyle("-fx-font-size: 15px;");

        getChildren().addAll(title, totalDocs, totalUsers, totalBorrows, mostPopular);
        VBox.setVgrow(this, Priority.ALWAYS);
    }
}
