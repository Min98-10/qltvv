package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DocumentListPane extends VBox {
    private final GridPane grid;
    private final List<Document> documents;

    public DocumentListPane(Consumer<Document> onDocumentSelected) {
        setPadding(new Insets(20));
        setSpacing(15);

        // ===== Header =====
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("📚 Danh sách tài liệu (Demo)");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button addBtn = new Button("➕ Thêm tài liệu");
        addBtn.setOnAction(e -> showAddDocumentForm());

        header.getChildren().addAll(title, addBtn);

        // ===== Grid tài liệu =====
        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        documents = new ArrayList<>(getMockDocuments());
        reloadGrid(onDocumentSelected);

        getChildren().addAll(header, grid);
    }

    private void reloadGrid(Consumer<Document> onDocumentSelected) {
        grid.getChildren().clear();
        int col = 0, row = 0;

        for (Document doc : documents) {
            VBox card = createDocumentCard(doc);
            card.setOnMouseClicked((MouseEvent e) -> onDocumentSelected.accept(doc));
            grid.add(card, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createDocumentCard(Document doc) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px;");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(150);

        ImageView img = new ImageView(new Image(doc.getImageUrl(), 100, 130, true, true));
        Label title = new Label(doc.getTitle());
        title.setWrapText(true);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        Label author = new Label(doc.getAuthor());
        author.setStyle("-fx-font-size: 12px;");

        card.getChildren().addAll(img, title, author);
        return card;
    }

    private void showAddDocumentForm() {
        // ===== Form thêm tài liệu (giả lập) =====
        Stage popup = new Stage();
        popup.setTitle("Thêm tài liệu (demo)");

        TextField titleField = new TextField();
        titleField.setPromptText("Tên tài liệu");

        TextField authorField = new TextField();
        authorField.setPromptText("Tác giả");

        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText("Link ảnh bìa (URL)");

        TextField categoryField = new TextField("Lập trình");
        TextField statusField = new TextField("Còn");
        TextArea summaryArea = new TextArea("Tóm tắt nội dung...");

        Button saveBtn = new Button("Lưu demo");
        saveBtn.setOnAction(e -> {
            Document newDoc = new Document(
                    titleField.getText(),
                    authorField.getText(),
                    imageUrlField.getText(),
                    categoryField.getText(),
                    statusField.getText(),
                    0,
                    "01/06/2025",
                    summaryArea.getText()
            );
            documents.add(newDoc);
            reloadGrid(doc -> {});
            popup.close();
        });

        VBox layout = new VBox(10,
                new Label("Tên tài liệu:"), titleField,
                new Label("Tác giả:"), authorField,
                new Label("Ảnh bìa (URL):"), imageUrlField,
                new Label("Thể loại:"), categoryField,
                new Label("Tình trạng:"), statusField,
                new Label("Tóm tắt:"), summaryArea,
                saveBtn
        );
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(400);

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.show();
    }

    private List<Document> getMockDocuments() {
        return List.of(
                new Document("Quỷ Tiến Hóa", "Nguyễn Văn A", "https://via.placeholder.com/100x130", "Lập trình", "Còn", 120, "01/05/2025", "Tài liệu về tiến hóa..."),
                new Document("Tâm lý học cơ bản", "Trần Thị B", "https://via.placeholder.com/100x130", "Tâm lý", "Còn", 89, "28/04/2025", "Khái niệm cơ bản tâm lý."),
                new Document("Cấu trúc dữ liệu", "Phạm Văn C", "https://via.placeholder.com/100x130", "Lập trình", "Hết", 200, "15/04/2025", "Tài liệu học cấu trúc."),
                new Document("Tài chính doanh nghiệp", "Hoàng Văn D", "https://via.placeholder.com/100x130", "Kinh tế", "Còn", 56, "02/05/2025", "Giáo trình tài chính.")
        );
    }
}
