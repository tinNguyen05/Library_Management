package com.example.library;

import com.example.library.book.Book;
import com.example.library.book.DetailsBookController;
import com.example.library.book.LibraryManageController;
import com.example.library.book.LibrarySearchController;
import com.example.library.user.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class MainApplication extends Application {
    private Stage primaryStage;
    private Stage splashStage;
    public static MediaPlayer backgroundMusicPlayer; // nhạc nền
    private static boolean isMusicPlaying = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showSplash();
        initializeBackgroundMusic();
    }

    public void showSplash() throws IOException {
        splashStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Splash.fxml"));
        Parent root = fxmlLoader.load();

        Splash splashController = fxmlLoader.getController();
        splashController.setMainApp(this);

        splashStage.setScene(new Scene(root));
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.show();
    }

    public void finishSplash() {
        if (splashStage != null) {
            splashStage.close();
        }
    }

    private void initializeBackgroundMusic() {
        if (backgroundMusicPlayer == null) {
            String musicFile = getClass().getResource("/com/example/library/music/pts.mp3").toString();
            Media media = new Media(musicFile);
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    public static void toggleBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            if (backgroundMusicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                isMusicPlaying = false;
                backgroundMusicPlayer.pause();
            } else {
                backgroundMusicPlayer.play();
                isMusicPlaying = true;
            }
        }
    }

    public static boolean isBackgroundMusicPlaying() {
        return isMusicPlaying;
    }

    public void showLoginClient() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LoginClient.fxml"));
        Parent root = fxmlLoader.load();

        LoginClientForm loginClientForm = fxmlLoader.getController();
        loginClientForm.setMainApp(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Client Login");

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    public void showLoginAdmin() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LoginAdmin.fxml"));
        Parent root = fxmlLoader.load();

        LoginAdminForm loginAdminForm = fxmlLoader.getController();
        loginAdminForm.setMainApp(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Admin Login");
        primaryStage.setScene(scene);

        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    public void showDetailBook(Book book) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DetailsBook.fxml"));
        Parent root = fxmlLoader.load();

        DetailsBookController detailsBookController = fxmlLoader.getController();
        detailsBookController.setMainApp(this);
        detailsBookController.setBookDetails(book);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Details Book");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showMainMenu() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainMenu.fxml"));
        Parent root = fxmlLoader.load();

        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setMainApp(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showSearchView() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LibrarySearch.fxml"));
        Parent root = fxmlLoader.load();

        LibrarySearchController searchController = fxmlLoader.getController();
        searchController.setMainApplication(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Search Books");

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    public void showManageView() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LibraryManage.fxml"));
        Parent root = fxmlLoader.load();

        LibraryManageController manageController = fxmlLoader.getController();
        manageController.setMainApplication(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Manage Books");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showUser() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("User.fxml"));
        Parent root = fxmlLoader.load();

        UserManagerSystem userManagerSystem = fxmlLoader.getController();
        userManagerSystem.setMainApp(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("User Management");

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    public void showSignUp() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("SignUp.fxml"));
        Parent root = fxmlLoader.load();

        SignupForm signupForm = fxmlLoader.getController();
        signupForm.setMainApp(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showBookBorrow() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("BorrowBook.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void showBookReturn() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ReturnBook.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    public void showChangePass() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ChangeP.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void showForgotPass() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ForgotP.fxml"));
        Parent root = fxmlLoader.load();

        ForgotP forgotP = fxmlLoader.getController();
        forgotP.setMainApp(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Forgot Password");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showHandleRequest() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("HandleRequest.fxml"));
        Parent root = fxmlLoader.load();

        HandleRequest handleRequest = fxmlLoader.getController();
        handleRequest.setMainApp(this);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Handle Request");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
