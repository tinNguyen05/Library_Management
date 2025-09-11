package com.example.library.user;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import com.example.library.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupForm extends CoreDatabase {

    private MainApplication mainApp;

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
    private TextField tClass;

    @FXML
    private TextField tName;

    @FXML
    private TextField tPhone;

    @FXML
    private TextField username;

    public void signup() throws Exception{
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();
        String enteredName = tName.getText();
        String enteredPhone = tPhone.getText();
        String enteredClass = tClass.getText();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty() || enteredName.isEmpty() || enteredPhone.isEmpty() || enteredClass.isEmpty()) {
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists!");
                alert.showAndWait();
            } else {
                String sql = "INSERT INTO userdata (username, password, name, role, phone, classname) VALUES (?, ?, ?, ?, ?, ?)";
                st = con.prepareStatement(sql);
                st.setString(1, enteredUsername);
                st.setString(2, enteredPassword);
                st.setString(3, enteredName);
                st.setString(4, "client");
                st.setString(5, enteredPhone);
                st.setString(6, enteredClass);


                int rowsAffected = st.executeUpdate();
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("User successfully registered!");
                    alert.showAndWait();

                    mainApp.showLoginClient();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while connecting to the database.");
            alert.showAndWait();
        }
    }




    @FXML
    private void openLoginClient() throws Exception {
        mainApp.showLoginClient();
    }

    public void setMainApp(MainApplication mainApplication) {
        this.mainApp = mainApplication;
    }
}
