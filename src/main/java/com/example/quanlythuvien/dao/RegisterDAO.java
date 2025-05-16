package com.example.quanlythuvien.dao;

import com.example.quanlythuvien.util.DatabaseHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterDAO {

    private static final Logger LOGGER = Logger.getLogger(RegisterDAO.class.getName());

    // ✅ Kiểm tra tên đăng nhập đã tồn tại chưa
    public static boolean usernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi kiểm tra tồn tại username: " + username, e);
            return true; // giả định đã tồn tại nếu lỗi
        }
    }

    // ✅ Đăng ký tài khoản mới với đầy đủ thông tin
    public static boolean registerUserWithInfo(String fullName, LocalDate birthDate, String id,
                                               String email, String address,
                                               String username, String password) {
        String sql = "INSERT INTO users (full_name, birth_date, id, email, address, username, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setDate(2, Date.valueOf(birthDate));
            stmt.setString(3, id); // cập nhật từ cccd → id
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, username);
            stmt.setString(7, password);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đăng ký tài khoản cho username: " + username, e);
            return false;
        }
    }

    public static ResultSet getUserInfo(String username) {
        String sql = "SELECT full_name, birth_date, id, email, address FROM users WHERE username = ?";
        try {
            Connection conn = DatabaseHelper.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean updateUserInfo(String username, String fullName, LocalDate birthDate,
                                         String id, String email, String address) {
        String sql = "UPDATE users SET full_name=?, birth_date=?, id=?, email=?, address=? WHERE username=?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setDate(2, Date.valueOf(birthDate));
            stmt.setString(3, id);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUserFullInfo(String currentUsername, String newUsername, String newPassword,
                                             String fullName, LocalDate birthDate, String id,
                                             String email, String address) {
        String sql = "UPDATE users SET username=?, password=?, full_name=?, birth_date=?, id=?, email=?, address=? " +
                "WHERE username=?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, fullName);
            stmt.setDate(4, Date.valueOf(birthDate));
            stmt.setString(5, id);
            stmt.setString(6, email);
            stmt.setString(7, address);
            stmt.setString(8, currentUsername);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
