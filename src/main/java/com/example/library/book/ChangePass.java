package com.example.library.book;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePass extends CoreDatabase {

    @FXML
    private AnchorPane changePassView;

    @FXML
    private TextField currentP;

    @FXML
    private TextField newPass;

    @FXML
    private TextField newPasss2;

    public void close() {
        ((Stage) changePassView.getScene().getWindow()).close();
    }

    public void changePass(ActionEvent event) {
        String currentPassword = currentP.getText();
        String newPassword = newPass.getText();
        String confirmPassword = newPasss2.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Password Mismatch", "New passwords do not match.");
            return;
        }
        if (checkCurrentPassword(currentPassword)) {
            if (updatePassword(newPassword)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
                close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to change password.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Current Password", "Current password is incorrect.");
        }


    }

    private boolean checkCurrentPassword(String currentPassword) {
        boolean isValid = false;
        try {
            String sql = "SELECT password FROM userdata WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, AccountDB.getId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(currentPassword)) {
                        isValid = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private boolean updatePassword(String newPassword) {
        boolean isUpdated = false;
        try {
            String sql = "UPDATE userdata SET password = ? WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, newPassword);
                stmt.setInt(2, AccountDB.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    isUpdated = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

