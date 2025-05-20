package com.example.quanlythuvien.view;

import com.example.quanlythuvien.api.GoogleBooksAPI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AddBookForm extends GridPane {

    private final TextField isbnField = new TextField();
    private final TextField titleField = new TextField();
    private final TextField authorField = new TextField();
    private final TextField categoryField = new TextField();
    private final TextField imageUrlField = new TextField(); // ✅ mới

    public AddBookForm() {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(20));

        add(new Label("ISBN:"), 0, 0);
        add(isbnField, 1, 0);

        Button fetchBtn = new Button("Tự động điền");
        fetchBtn.setOnAction(_ -> fetchFromGoogle());
        add(fetchBtn, 2, 0);

        add(new Label("Tiêu đề:"), 0, 1);
        add(titleField, 1, 1, 2, 1);

        add(new Label("Tác giả:"), 0, 2);
        add(authorField, 1, 2, 2, 1);

        add(new Label("Thể loại:"), 0, 3);
        add(categoryField, 1, 3, 2, 1);

        add(new Label("Ảnh bìa (URL):"), 0, 4);
        add(imageUrlField, 1, 4, 2, 1);
    }

    private void fetchFromGoogle() {
        String isbn = isbnField.getText().trim();
        if (isbn.isEmpty()) return;

        new Thread(() -> {
            GoogleBooksAPI.BookInfo info = GoogleBooksAPI.fetchByISBN(isbn);
            Platform.runLater(() -> {
                if (info != null) {
                    titleField.setText(info.title);
                    authorField.setText(info.author);
                    imageUrlField.setText(info.imageUrl); // ✅ tự động điền ảnh
                } else {
                    new Alert(Alert.AlertType.WARNING, "Không tìm thấy sách.").show();
                }
            });
        }).start();
    }

    public String getTitle() {
        return titleField.getText().trim();
    }

    public String getAuthor() {
        return authorField.getText().trim();
    }

    public String getCategory() {
        return categoryField.getText().trim();
    }

    public String getImageUrl() {
        return imageUrlField.getText().trim();
    }
}
