package com.example.quanlythuvien.view;

import com.example.quanlythuvien.dao.DocumentFileDAO;
import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DocumentListPane extends VBox {
    private final GridPane grid;
    private final List<Document> allDocuments;
    private final Consumer<Document> onSelected;

    public DocumentListPane(Consumer<Document> onDocumentSelected) {
        this.onSelected = onDocumentSelected;

        setPadding(new Insets(20));
        setSpacing(15);

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("📚 Danh sách tài liệu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button addBtn = new Button("➕ Thêm tài liệu");
        addBtn.setOnAction(_ -> showAddDocumentForm());

        header.getChildren().addAll(title, addBtn);

        grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        allDocuments = new ArrayList<>(DocumentFileDAO.getAll());
        reloadGrid(allDocuments);

        getChildren().addAll(header, grid);
    }

    private void reloadGrid(List<Document> docs) {
        grid.getChildren().clear();
        int col = 0, row = 0;

        for (Document doc : docs) {
            VBox card = createDocumentCard(doc);
            card.setOnMouseClicked((MouseEvent e) -> onSelected.accept(doc));
            grid.add(card, col, row);
            col++;
            if (col == 6) {
                col = 0;
                row++;
            }
        }
    }

    private void reloadGrid() {
        allDocuments.clear();
        allDocuments.addAll(DocumentFileDAO.getAll());
        reloadGrid(allDocuments);
    }

    public void filterDocumentsByKeyword(String keyword) {
        List<Document> filtered = allDocuments.stream()
                .filter(doc -> doc.getTitle().toLowerCase().contains(keyword)
                        || doc.getAuthor().toLowerCase().contains(keyword)
                        || doc.getCategory().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        reloadGrid(filtered);
    }

    private VBox createDocumentCard(Document doc) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #ccc; -fx-background-color: white; -fx-border-radius: 8px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(160);
        card.setPrefHeight(300);

        ImageView img;
        try {
            img = new ImageView(new Image(doc.getImageUrl(), 120, 160, true, true));
        } catch (Exception e) {
            img = new ImageView(new Image("https://via.placeholder.com/120x160"));
        }

        Label title = new Label(doc.getTitle());
        title.setWrapText(true);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        Label author = new Label(doc.getAuthor());
        author.setStyle("-fx-font-size: 12px;");

        Button deleteBtn = new Button("❌ Xoá");
        deleteBtn.setStyle("-fx-font-size: 11px;");
        deleteBtn.setOnAction(e -> {
            DocumentFileDAO.remove(doc);
            reloadGrid();
        });

        card.getChildren().addAll(img, title, author, deleteBtn);
        return card;
    }

    private void showAddDocumentForm() {
        Stage popup = new Stage();
        popup.setTitle("➕ Thêm tài liệu");

        Runnable onSuccess = () -> {
            reloadGrid();
            popup.close();
        };

        AddDocumentPane formPane = new AddDocumentPane(onSuccess);
        VBox layout = new VBox(15, formPane);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.show();
    }
}
