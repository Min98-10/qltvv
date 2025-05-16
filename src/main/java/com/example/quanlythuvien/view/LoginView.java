package com.example.quanlythuvien.view;

import com.example.quanlythuvien.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {

    public void show(Stage stage) {
        Label title = new Label("Đăng nhập hệ thống");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Tên đăng nhập");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mật khẩu");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);
        errorLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        errorLabel.setAlignment(Pos.CENTER);


        Button loginBtn = new Button("Đăng nhập");
        Button registerBtn = new Button("Đăng ký tài khoản");

        // Controller
        LoginController controller = new LoginController(stage);
        loginBtn.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();
            controller.handleLogin(user, pass, errorLabel);
        });

        registerBtn.setOnAction(e -> controller.goToRegister());

        // Layout form
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER);
        form.add(new Label("Tên đăng nhập:"), 0, 0);
        form.add(usernameField, 1, 0);
        form.add(new Label("Mật khẩu:"), 0, 1);
        form.add(passwordField, 1, 1);

        // Giao diện tổng thể
        VBox layout = new VBox(15, title, form, loginBtn, registerBtn, errorLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setMaxWidth(400);
        layout.setPrefWidth(350);

        StackPane wrapper = new StackPane(layout);
        wrapper.setPadding(new Insets(20));

        Scene scene = new Scene(wrapper, 700, 500);
        stage.setTitle("Thư viện - Đăng nhập");
        stage.setScene(scene);
        stage.show();
    }
}
