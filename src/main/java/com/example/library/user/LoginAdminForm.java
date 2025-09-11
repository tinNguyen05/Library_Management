package com.example.library.user;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import com.example.library.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginAdminForm extends CoreDatabase {

    @FXML
    private ImageView adminView;
    private MainApplication mainApp;

    public void initialize(URL url, ResourceBundle rb) {
        Image clientImage = new Image(getClass().getResource("image/admin.png").toExternalForm());
        adminView.setImage(clientImage);
    }

    @FXML
    private Button btnLoginADmin;

    @FXML
    private Button btnReturnClient;

    @FXML
    private AnchorPane loginadmin_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    public void loginAdmin() {
        String sql = "SELECT * FROM userdata WHERE username = ? and password = ? and role = 'admin'";

        con = AccountDB.getInstance().getConnection();

        try {
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
                return;
            }

            st = con.prepareStatement(sql);
            st.setString(1, username.getText());
            st.setString(2, password.getText());

            rs = st.executeQuery();

            Alert alert;
            if (rs.next()) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Login!");
                alert.showAndWait();
                mainApp.showUser();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Wrong Username/Password!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while connecting to the database.");
            alert.showAndWait();
        }
    }

    @FXML
    public void openLoginClient() throws Exception{
        mainApp.showLoginClient();
    }

    public void setMainApp(MainApplication mainApplication) {
        this.mainApp = mainApplication;
    }
}
