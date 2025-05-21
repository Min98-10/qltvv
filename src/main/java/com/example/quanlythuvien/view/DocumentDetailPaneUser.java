package com.example.quanlythuvien.view;

import com.example.quanlythuvien.model.BorrowRecord;
import com.example.quanlythuvien.model.Comment;
import com.example.quanlythuvien.model.Document;
import com.example.quanlythuvien.util.BorrowDataManager;
import com.example.quanlythuvien.util.CommentDataManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentDetailPaneUser extends BorderPane {

    private final VBox centerBox;
    private final VBox rightBox;
    private final ListView<String> commentList;
    private final Label avgRatingLabel;

    public DocumentDetailPaneUser(Document doc, String username) {
        setPadding(new Insets(20));

        centerBox = new VBox(10);
        centerBox.setPadding(new Insets(20));

        Label titleLabel = new Label(doc.getTitle());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(doc.getImageUrl()));
        } catch (Exception e) {
            imageView.setImage(new Image("https://via.placeholder.com/160x220"));
        }
        imageView.setFitWidth(220);
        imageView.setPreserveRatio(true);

        Label updatedLabel = new Label("🕒 Cập nhật: " + doc.getUpdatedAt());
        Label authorLabel = new Label("✍ Tác giả: " + doc.getAuthor());
        Label categoryLabel = new Label("📂 Thể loại: " + doc.getCategory());
        Label statusLabel = new Label("📦 Tình trạng: " + doc.getStatus());
        Label viewsLabel = new Label("👁️ Lượt mượn: " + doc.getViewCount());

        Button borrowBtn = new Button("📥 Mượn tài liệu");
        borrowBtn.setStyle("-fx-font-size: 14px;");
        borrowBtn.setOnAction(e -> showBorrowDialog(doc, username));

        VBox infoBox = new VBox(8, updatedLabel, authorLabel, categoryLabel, statusLabel, viewsLabel, borrowBtn);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-font-size: 15px;");

        HBox topSection = new HBox(30, imageView, infoBox);
        topSection.setAlignment(Pos.CENTER_LEFT);

        Label commentLabel = new Label("💬 BÌNH LUẬN & ĐÁNH GIÁ");
        commentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        commentList = new ListView<>();
        updateComments(doc.getTitle());

        TextField commentField = new TextField();
        commentField.setPromptText("Viết bình luận...");

        ComboBox<Integer> starBox = new ComboBox<>();
        starBox.getItems().addAll(1, 2, 3, 4, 5);
        starBox.setPromptText("Chọn sao (1–5)");

        Button sendBtn = new Button("Gửi");
        sendBtn.setOnAction(e -> {
            String content = commentField.getText().trim();
            Integer stars = starBox.getValue();
            if (!content.isEmpty() && stars != null) {
                Comment c = new Comment(username, doc.getTitle(), content, LocalDate.now().toString(), stars);
                CommentDataManager.getInstance().add(c);
                updateComments(doc.getTitle());
                updateAverageRating(doc.getTitle());
                commentField.clear();
                starBox.setValue(null);
            } else {
                new Alert(Alert.AlertType.WARNING, "Vui lòng nhập bình luận và chọn sao!").show();
            }
        });

        HBox commentBox = new HBox(5, commentField, starBox, sendBtn);
        commentBox.setAlignment(Pos.CENTER_LEFT);

        centerBox.getChildren().addAll(titleLabel, topSection, commentLabel, commentList, commentBox);

        rightBox = new VBox(10);
        rightBox.setPadding(new Insets(10));
        rightBox.setStyle("-fx-background-color: #f5f5f5;");
        rightBox.setPrefWidth(260);

        Label ratingLabel = new Label("⭐ Đánh giá trung bình");
        ratingLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        avgRatingLabel = new Label("★ " + CommentDataManager.getInstance().getAverageStars(doc.getTitle()) + " / 5");

        VBox ratingBox = new VBox(5, ratingLabel, avgRatingLabel);
        VBox borrowBox = createBorrowBox(username, doc.getTitle());
        rightBox.getChildren().addAll(ratingBox, borrowBox);

        setCenter(centerBox);
        setRight(rightBox);
    }

    private void showBorrowDialog(Document doc, String username) {
        Stage popup = new Stage();
        popup.setTitle("Xác nhận mượn tài liệu");

        String borrowDate = LocalDate.now().toString();

        Label docTitle = new Label("📘 Tên tài liệu: " + doc.getTitle());
        Label borrowDateLabel = new Label("📅 Ngày mượn: " + borrowDate);

        Label dayLabel = new Label("🗓️ Số ngày mượn:");
        Spinner<Integer> daysSpinner = new Spinner<>(1, 60, 14);
        daysSpinner.setEditable(true);

        Label quantityLabel = new Label("Số lượng:");
        TextField quantityField = new TextField("1");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        Button confirmBtn = new Button("✅ Gửi yêu cầu");
        confirmBtn.setOnAction(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int days = daysSpinner.getValue();
                String dueDate = LocalDate.now().plusDays(days).toString();

                BorrowRecord record = new BorrowRecord(username, doc.getTitle(), borrowDate, dueDate, "Chờ duyệt", quantity);
                BorrowDataManager.add(record);

                popup.close();
                showInfo("Yêu cầu mượn đã được gửi và đang chờ admin duyệt.");
                updateBorrowHistory(username, doc.getTitle());

            } catch (NumberFormatException ex) {
                messageLabel.setText("⚠️ Số lượng không hợp lệ!");
            }
        });

        VBox layout = new VBox(12,
                docTitle,
                borrowDateLabel,
                new HBox(10, dayLabel, daysSpinner),
                new HBox(10, quantityLabel, quantityField),
                confirmBtn,
                messageLabel
        );
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(400);

        popup.setScene(new Scene(layout));
        popup.show();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateComments(String documentTitle) {
        List<String> comments = CommentDataManager.getInstance().getByDocument(documentTitle).stream()
                .map(c -> c.getUsername() + " (★" + c.getStars() + ", " + c.getDate() + "): " + c.getContent())
                .collect(Collectors.toList());
        commentList.setItems(FXCollections.observableArrayList(comments));
    }

    private void updateAverageRating(String documentTitle) {
        avgRatingLabel.setText("★ " + CommentDataManager.getInstance().getAverageStars(documentTitle) + " / 5");
    }

    private VBox createBorrowBox(String username, String documentTitle) {
        Label borrowedTitle = new Label("📚 Lịch sử mượn tài liệu này");
        borrowedTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

        List<BorrowRecord> userRecords = BorrowDataManager.findByUsername(username);
        List<String> borrowHistory = userRecords.stream()
                .filter(r -> r.getDocumentTitle().equals(documentTitle))
                .map(r -> {
                    String returnText = r.getStatus().equalsIgnoreCase("Đã trả") ? r.getDueDate() : "Chưa trả";
                    return "• Mượn: " + r.getBorrowDate() + " → Trả: " + returnText + " (SL: " + r.getQuantity() + ")";
                })
                .collect(Collectors.toList());

        ListView<String> borrowedList = new ListView<>(FXCollections.observableArrayList(borrowHistory));
        borrowedList.setPrefHeight(130);

        return new VBox(5, borrowedTitle, borrowedList);
    }

    private void updateBorrowHistory(String username, String documentTitle) {
        VBox updatedBorrowBox = createBorrowBox(username, documentTitle);
        rightBox.getChildren().removeIf(node -> node instanceof VBox && ((VBox) node).getChildren().stream()
                .anyMatch(child -> child instanceof Label && ((Label) child).getText().contains("Lịch sử mượn")));
        rightBox.getChildren().add(updatedBorrowBox);
    }
}
