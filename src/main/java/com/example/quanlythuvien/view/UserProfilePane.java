package com.example.quanlythuvien.view;

import com.example.quanlythuvien.dao.RegisterDAO;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.time.LocalDate;

public class UserProfilePane extends VBox {

    private final TextField nameField, idField, emailField, addressField, usernameField;
    private final PasswordField passwordField;
    private final DatePicker birthDatePicker;
    private final Label statusLabel;
    private String currentUsername;

    public UserProfilePane(String username) {
        this.currentUsername = username;

        setSpacing(15);
        setPadding(new Insets(20));

        Label title = new Label("👤 Thông tin tài khoản");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        nameField = createDisabledField();
        birthDatePicker = new DatePicker();
        birthDatePicker.setDisable(true);
        birthDatePicker.setStyle("-fx-font-size: 14px;");

        idField = createDisabledField();
        emailField = createDisabledField();
        addressField = createDisabledField();
        usernameField = createDisabledField();
        passwordField = new PasswordField();
        passwordField.setEditable(false);
        passwordField.setStyle("-fx-font-size: 14px;");

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");

        Button editBtn = new Button("✏️ Chỉnh sửa");
        Button saveBtn = new Button("✅ Lưu");

        editBtn.setOnAction(e -> setEditable(true));
        saveBtn.setOnAction(e -> {
            String newUsername = usernameField.getText().trim();
            boolean success = RegisterDAO.updateUserFullInfo(
                    currentUsername,
                    newUsername,
                    passwordField.getText(),
                    nameField.getText(),
                    birthDatePicker.getValue(),
                    idField.getText(),
                    emailField.getText(),
                    addressField.getText()
            );
            if (success) {
                statusLabel.setText("✅ Cập nhật thành công!");
                setEditable(false);

                // Nếu đổi username → load lại
                if (!newUsername.equals(currentUsername)) {
                    currentUsername = newUsername;
                    Platform.runLater(() -> {
                        this.getChildren().clear();
                        new UserProfilePane(currentUsername).showIn(this);
                    });
                }

            } else {
                statusLabel.setText("❌ Lỗi khi cập nhật!");
            }
        });

        loadUserData(currentUsername);

        getChildren().addAll(
                title,
                labelAndField("Họ và tên:", nameField),
                labelAndField("Ngày sinh:", birthDatePicker),
                labelAndField("Mã định danh (ID):", idField),
                labelAndField("Email:", emailField),
                labelAndField("Địa chỉ:", addressField),
                labelAndField("Tên đăng nhập:", usernameField),
                labelAndField("Mật khẩu mới:", passwordField),
                new HBox(10, editBtn, saveBtn),
                statusLabel
        );
    }

    private void setEditable(boolean b) {
        nameField.setEditable(b);
        birthDatePicker.setDisable(!b);
        idField.setEditable(b);
        emailField.setEditable(b);
        addressField.setEditable(b);
        usernameField.setEditable(b);
        passwordField.setEditable(b);
    }

    private void loadUserData(String username) {
        if (username.equals("nam")) {
            nameField.setText("Nam Admin");
            birthDatePicker.setValue(LocalDate.of(2000, 1, 1));
            idField.setText("ADMIN001");
            emailField.setText("nam.admin@example.com");
            addressField.setText("Trụ sở chính");
            usernameField.setText("nam");
            passwordField.setText("nam123");
            setEditable(false);
        } else {
            try {
                ResultSet rs = RegisterDAO.getUserInfo(username);
                if (rs != null && rs.next()) {
                    nameField.setText(rs.getString("full_name"));
                    birthDatePicker.setValue(rs.getDate("birth_date").toLocalDate());
                    idField.setText(rs.getString("id"));
                    emailField.setText(rs.getString("email"));
                    addressField.setText(rs.getString("address"));
                    usernameField.setText(username);
                    passwordField.setText(rs.getString("password"));
                    setEditable(false);
                } else {
                    statusLabel.setText("Không tìm thấy người dùng.");
                }
            } catch (Exception e) {
                statusLabel.setText("Lỗi khi tải thông tin.");
                e.printStackTrace();
            }
        }
    }

    private TextField createDisabledField() {
        TextField f = new TextField();
        f.setEditable(false);
        f.setStyle("-fx-font-size: 14px;");
        return f;
    }

    private VBox labelAndField(String labelText, Control field) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 15px;");
        return new VBox(5, label, field);
    }

    // Hỗ trợ reload nội dung sau khi đổi username
    public void showIn(VBox parent) {
        parent.getChildren().setAll(this.getChildren());
    }
}
