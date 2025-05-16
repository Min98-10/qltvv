package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.Document;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class DocumentDetailPane extends VBox {

    private final Label titleLabel;
    private final Label authorLabel;
    private final Label categoryLabel;
    private final Label statusLabel;
    private final Label viewsLabel;
    private final Label updatedLabel;
    private final Label summaryLabel;
    private final ImageView imageView;

    public DocumentDetailPane() {
        setPadding(new Insets(20));
        setSpacing(20);

        // ===== Tiêu đề tài liệu =====
        titleLabel = new Label("TÊN TÀI LIỆU MẪU");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        // ===== Ảnh tài liệu =====
        imageView = new ImageView("https://via.placeholder.com/160x220");
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(220);

        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.TOP_CENTER);
        imageBox.setPadding(new Insets(10));
        HBox.setHgrow(imageBox, Priority.ALWAYS);

        // ===== Thông tin chi tiết =====
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-font-size: 15px;");

        updatedLabel = new Label("🕒 Cập nhật: 01/05/2025");
        authorLabel = new Label("✍ Tác giả: Nguyễn Văn A");
        categoryLabel = new Label("📂 Thể loại: Lập trình");
        statusLabel = new Label("📦 Tình trạng: Còn");
        viewsLabel = new Label("👁️ Lượt mượn: 123");

        HBox actionBox = new HBox(20);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        Button addBtn = new Button("Thêm");
        Button editBtn = new Button("Sửa");
        Button deleteBtn = new Button("Xoá");
        actionBox.getChildren().addAll(addBtn, editBtn, deleteBtn);

        infoBox.getChildren().addAll(updatedLabel, authorLabel, categoryLabel, statusLabel, viewsLabel, actionBox);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // ===== Layout ảnh + thông tin =====
        HBox topSection = new HBox(30, imageBox, infoBox);
        topSection.setAlignment(Pos.CENTER);
        HBox.setHgrow(topSection, Priority.ALWAYS);

        // ===== Tóm tắt nội dung =====
        Label summaryTitle = new Label("📄 TÓM TẮT");
        summaryTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        summaryLabel = new Label("Đây là tóm tắt mẫu cho tài liệu. Bạn có thể cập nhật nội dung sau.");
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle("-fx-font-size: 14px;");
        summaryLabel.setMaxWidth(Double.MAX_VALUE);

        // ===== Bình luận =====
        Button commentBtn = new Button("💬 Xem bình luận");

        VBox summaryBox = new VBox(10, summaryTitle, summaryLabel, commentBtn);
        summaryBox.setPadding(new Insets(10));

        // ===== Tổng layout =====
        getChildren().addAll(titleLabel, topSection, summaryBox);
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    // ✅ Gán dữ liệu từ Document
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
