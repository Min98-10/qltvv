package com.example.quanlythuvien.controller;

import com.example.quanlythuvien.dao.LoginDAO;
import com.example.quanlythuvien.view.LoginView;
import com.example.quanlythuvien.view.MainAdminView;
import com.example.quanlythuvien.view.MainUserView;
import com.example.quanlythuvien.view.RegisterView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController {
    private final Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void handleLogin(String username, String password, Label errorLabel) {
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            return;
        }

        // ✅ Tài khoản admin mặc định (chỉ 1 người dùng)
        if (username.equals("admin") && password.equals("admin123")) {
            new MainAdminView().show(stage);
            return;
        }

        // ✅ Tài khoản vĩnh viễn đặc biệt (không phụ thuộc DB)
        if (username.equals("nam") && password.equals("nam123")) {
            new MainUserView(username).show(stage); // Truyền tên người dùng nếu cần
            return;
        }

        // ✅ Tài khoản từ DB
        boolean valid = LoginDAO.checkLogin(username, password);
        if (valid) {
            new MainUserView(username).show(stage);
        } else {
            errorLabel.setText("Sai tên đăng nhập hoặc mật khẩu.");
        }
    }

    public void goToRegister() {
        new RegisterView().show(stage);
    }
}
