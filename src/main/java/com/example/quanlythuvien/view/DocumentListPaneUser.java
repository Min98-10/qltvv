package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;
import java.util.function.Consumer;

public class DocumentListPaneUser extends VBox {

    public DocumentListPaneUser(Consumer<Document> onDocumentSelected) {
        setPadding(new Insets(20));
        setSpacing(15);

        Label title = new Label("📚 Tài liệu có sẵn");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        getChildren().add(title);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        List<Document> documents = getMockDocuments();
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

        getChildren().add(grid);
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

    private List<Document> getMockDocuments() {
        return List.of(
                new Document("Java cơ bản", "Nguyễn A", "https://via.placeholder.com/100x130", "Lập trình", "Còn", 123, "01/06/2025", "Giới thiệu Java cho người mới."),
                new Document("Kỹ năng mềm", "Lê B", "https://via.placeholder.com/100x130", "Kỹ năng", "Còn", 87, "02/06/2025", "Tài liệu rèn luyện giao tiếp."),
                new Document("Tâm lý học", "Phạm C", "https://via.placeholder.com/100x130", "Tâm lý", "Còn", 45, "29/05/2025", "Tổng quan ngành tâm lý.")
        );
    }
}
