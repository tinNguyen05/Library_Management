package com.example.library.user;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import com.example.library.MainApplication;
import com.example.library.book.DetailsBookController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//public static User mainUser = null;

public class LoginClientForm extends CoreDatabase {

    public static int currentId;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private ImageView clientView;

    @FXML
    private ImageView welcome;

    private MainApplication mainApp;

    private UserManagerSystem userManagerSystem = new UserManagerSystem();

    public void initialize(URL url, ResourceBundle rb) {
        Image clientImage = new Image(getClass().getResource("image/client.png").toExternalForm());
        clientView.setImage(clientImage);

        Image welImage = new Image(getClass().getResource("image/welcome.png").toExternalForm());
        welcome.setImage(welImage);
    }

    public void loginClient(){

        String sql = "SELECT * FROM userdata WHERE username = ? and password = ? and role = 'client'";

        con = AccountDB.getInstance().getConnection();

        try{
            Alert alert;

            st = con.prepareStatement(sql);
            st.setString(1, username.getText());
            st.setString(2, password.getText());

            rs = st.executeQuery();


            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                if(rs.next()){

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();

                    currentId = userManagerSystem.searchUserName(username.getText());
                    AccountDB.setId(currentId);
                    mainApp.showMainMenu();

                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password!");
                    alert.showAndWait();

                }
            }
        }catch(Exception e){e.printStackTrace();}

    }

    @FXML
    public void openSignupForm() throws Exception {
        mainApp.showSignUp();
    }

    @FXML
    public void openForgotPassForm() throws Exception {
        mainApp.showForgotPass();
    }

    @FXML
    public void openLoginAdmin() throws Exception {
        try {
            mainApp.showLoginAdmin();
        } catch (Exception e) {
            System.out.println("main rong" + e.getMessage());
        }
    }

    public void setMainApp(MainApplication mainApplication) {
        this.mainApp = mainApplication;
    }
}


