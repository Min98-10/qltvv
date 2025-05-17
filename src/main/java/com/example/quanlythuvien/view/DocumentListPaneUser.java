package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class DocumentDetailPaneUser extends VBox {

    private final Label titleLabel;
    private final Label authorLabel;
    private final Label categoryLabel;
    private final Label statusLabel;
    private final Label viewsLabel;
    private final Label updatedLabel;
    private final Label summaryLabel;
    private final ImageView imageView;

    public DocumentDetailPaneUser() {
        setPadding(new Insets(20));
        setSpacing(20);

        titleLabel = new Label("TÊN TÀI LIỆU");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        imageView = new ImageView("https://via.placeholder.com/160x220");
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(220);

        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.TOP_CENTER);
        imageBox.setPadding(new Insets(10));
        HBox.setHgrow(imageBox, Priority.ALWAYS);

        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-font-size: 15px;");

        updatedLabel = new Label("🕒 Cập nhật: ");
        authorLabel = new Label("✍ Tác giả: ");
        categoryLabel = new Label("📂 Thể loại: ");
        statusLabel = new Label("📦 Tình trạng: ");
        viewsLabel = new Label("👁️ Lượt mượn: ");

        Button borrowBtn = new Button("📥 Mượn tài liệu");
        borrowBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        borrowBtn.setMaxWidth(Double.MAX_VALUE);

        infoBox.getChildren().addAll(updatedLabel, authorLabel, categoryLabel, statusLabel, viewsLabel, borrowBtn);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox topSection = new HBox(30, imageBox, infoBox);
        topSection.setAlignment(Pos.CENTER);

        Label summaryTitle = new Label("📄 TÓM TẮT");
        summaryTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        summaryLabel = new Label("(Chưa có nội dung tóm tắt)");
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle("-fx-font-size: 14px;");
        summaryLabel.setMaxWidth(Double.MAX_VALUE);

        Button commentBtn = new Button("💬 Xem bình luận");
        commentBtn.setStyle("-fx-font-size: 13px;");

        VBox summaryBox = new VBox(10, summaryTitle, summaryLabel, commentBtn);
        summaryBox.setPadding(new Insets(10));

        getChildren().addAll(titleLabel, topSection, summaryBox);
    }

    public void setData(Document doc) {
        titleLabel.setText(doc.getTitle());
        authorLabel.setText("✍ Tác giả: " + doc.getAuthor());
        categoryLabel.setText("📂 Thể loại: " + doc.getCategory());
        statusLabel.setText("📦 Tình trạng: " + doc.getStatus());
        viewsLabel.setText("👁️ Lượt mượn: " + doc.getViewCount());
        updatedLabel.setText("🕒 Cập nhật: " + doc.getUpdatedAt());
        summaryLabel.setText(doc.getSummary());

        try {
            imageView.setImage(new Image(doc.getImageUrl()));
        } catch (Exception e) {
            imageView.setImage(new Image("https://via.placeholder.com/160x220"));
        }
    }
}
