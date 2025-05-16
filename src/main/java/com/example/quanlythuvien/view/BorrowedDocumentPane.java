package com.example.quanlythuvien.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BorrowedDocumentPane extends VBox {

    public BorrowedDocumentPane() {
        setSpacing(15);
        setPadding(new Insets(20));
        setFillWidth(true);
        VBox.setVgrow(this, Priority.ALWAYS);

        Label title = new Label("📖 Tài liệu đã mượn");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox list = new VBox(15);
        list.setPadding(new Insets(10));

        list.getChildren().addAll(
                createCard("Java cơ bản", "01/06/2025", "10/06/2025", "✅ Còn hạn"),
                createCard("Tâm lý học", "25/05/2025", "01/06/2025", "🟠 Sắp đến hạn"),
                createCard("Kỹ năng mềm", "10/05/2025", "20/05/2025", "🔴 Quá hạn")
        );

        ScrollPane scrollPane = new ScrollPane(list);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;");

        getChildren().addAll(title, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    private HBox createCard(String name, String borrowDate, String dueDate, String status) {
        VBox info = new VBox(5);
        info.getChildren().addAll(
                new Label("📘 " + name),
                new Label("Ngày mượn: " + borrowDate),
                new Label("Hạn trả: " + dueDate),
                new Label("Trạng thái: " + status)
        );

        Button returnBtn = new Button("🔁 Trả tài liệu");
        Button extendBtn = new Button("📆 Gia hạn");

        HBox btnBox = new HBox(10, returnBtn, extendBtn);

        VBox content = new VBox(10, info, btnBox);
        content.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(content);
        card.setStyle("-fx-border-color: #ccc; -fx-padding: 12; -fx-background-color: #fefefe;");
        card.setAlignment(Pos.CENTER_LEFT);
        return card;
    }
}
