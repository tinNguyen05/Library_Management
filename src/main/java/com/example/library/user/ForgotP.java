package com.example.library.user;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import com.example.library.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Random;

public class ForgotP extends CoreDatabase {

    @FXML
    private Button btnReturnLogin;

    @FXML
    private Button btnSignUp;

    @FXML
    private PasswordField password;

    @FXML
    private AnchorPane signUpForm;

    @FXML
    private ImageView signupView;

    @FXML
    private TextField tPhone;

    @FXML
    private TextField username;

    private MainApplication mainApp;

    @FXML
    private void openLoginClient() throws Exception {
        mainApp.showLoginClient();
    }

    public void setMainApp(MainApplication mainApplication) {
        this.mainApp = mainApplication;
    }


    public void forgot() throws Exception {
        String enteredUsername = username.getText();
        String enteredPhone = tPhone.getText();

        if (enteredUsername.isEmpty() || enteredPhone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields!");
            alert.showAndWait();
            return;
        }

        String checkUserExistenceSql = "SELECT * FROM userdata WHERE username = ?";
        con = AccountDB.getInstance().getConnection();


        try {
            st = con.prepareStatement(checkUserExistenceSql);
            st.setString(1, enteredUsername);
            rs = st.executeQuery();

            if (rs.next()) {
                String phone = rs.getString("phone");

                if (phone.equals(enteredPhone)) {
                    String newPassword = generateRandomPassword();

                    String updatePasswordSql = "UPDATE userdata SET password = ? WHERE username = ?";
                    st = con.prepareStatement(updatePasswordSql);
                    st.setString(1, newPassword);
                    st.setString(2, enteredUsername);
                    int row = st.executeUpdate();

                    if (row > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Your new password is: " + newPassword);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to update password. Please try again.");
                        alert.showAndWait();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The phone number does not match.");
                    alert.showAndWait();
                }

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("User not found.");
                alert.showAndWait();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while accessing the database.");
            alert.showAndWait();
        }
    }
    private String generateRandomPassword() {
        Random random = new Random();
        int randomPassword = 10000 + random.nextInt(90000);
        return String.valueOf(randomPassword);
    }
}
