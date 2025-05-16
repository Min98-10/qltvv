package com.example.quanlythuvien.view;

import com.example.quanlythuvien.controller.RegisterController;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView {

    public void show(Stage stage) {
        Label title = new Label("Đăng ký thành viên");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Khối 1: Thông tin cá nhân
        TextField fullNameField = new TextField();
        DatePicker birthDatePicker = new DatePicker();
        TextField idField = new TextField(); // Đổi từ cccdField
        TextField emailField = new TextField();
        TextField addressField = new TextField();

        // Khối 2: Tài khoản
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button registerBtn = new Button("Đăng ký");
        Button backBtn = new Button("Quay lại đăng nhập");

        RegisterController controller = new RegisterController(stage);
        registerBtn.setOnAction(e -> {
            controller.handleFullRegister(
                    fullNameField.getText(),
                    birthDatePicker.getValue(),
                    idField.getText(), // dùng id
                    emailField.getText(),
                    addressField.getText(),
                    usernameField.getText(),
                    passwordField.getText(),
                    confirmPasswordField.getText(),
                    errorLabel
            );
        });

        backBtn.setOnAction(e -> controller.backToLogin());

        // Giao diện biểu mẫu
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(15);
        form.setPadding(new Insets(10));
        form.setAlignment(Pos.CENTER);

        form.add(new Label("Họ và tên:"), 0, 0);
        form.add(fullNameField, 1, 0);

        form.add(new Label("Ngày sinh:"), 0, 1);
        form.add(birthDatePicker, 1, 1);

        form.add(new Label("ID:"), 0, 2);
        form.add(idField, 1, 2); // Đổi nhãn và trường nhập

        form.add(new Label("Email:"), 0, 3);
        form.add(emailField, 1, 3);

        form.add(new Label("Địa chỉ:"), 0, 4);
        form.add(addressField, 1, 4);

        form.add(new Separator(), 0, 5, 2, 1);

        form.add(new Label("Tên đăng nhập:"), 0, 6);
        form.add(usernameField, 1, 6);

        form.add(new Label("Mật khẩu:"), 0, 7);
        form.add(passwordField, 1, 7);

        form.add(new Label("Xác nhận mật khẩu:"), 0, 8);
        form.add(confirmPasswordField, 1, 8);

        HBox buttonBox = new HBox(15, registerBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, title, form, buttonBox, errorLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setMaxWidth(500);
        root.setPrefWidth(400);

        StackPane wrapper = new StackPane(root);
        wrapper.setPadding(new Insets(20));

        Scene scene = new Scene(wrapper, 700, 600);
        stage.setScene(scene);
        stage.setTitle("Thư viện - Đăng ký thành viên");
        stage.show();
    }
}
