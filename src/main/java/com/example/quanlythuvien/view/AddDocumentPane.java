package com.example.quanlythuvien.view;

import com.example.quanlythuvien.dao.DocumentFileDAO;
import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddDocumentPane extends VBox {

    public AddDocumentPane(Runnable onSuccess) {
        setPadding(new Insets(20));
        setSpacing(15);

        Label title = new Label("📘 Thêm tài liệu mới");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        AddBookForm form = new AddBookForm();

        Button saveBtn = new Button("Lưu tài liệu");
        saveBtn.setOnAction(_ -> {
            String titleText = form.getTitle();
            String author = form.getAuthor();
            String category = form.getCategory();
            String imageUrl = form.getImageUrl(); // ✅ lấy thêm ảnh bìa

            if (titleText.isEmpty() || author.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Vui lòng điền đủ thông tin!").show();
                return;
            }

            Document doc = new Document(
                    titleText,
                    author,
                    imageUrl,
                    category,
                    "Còn",
                    0,
                    "01/06/2025",
                    ""
            );

            DocumentFileDAO.add(doc);
            new Alert(Alert.AlertType.INFORMATION, "Đã thêm tài liệu!").show();

            if (onSuccess != null) {
                onSuccess.run(); // Reload + đóng popup
            }
        });

        getChildren().addAll(title, form, saveBtn);
    }
}
