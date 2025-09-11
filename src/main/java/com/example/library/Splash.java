package com.example.library;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class Splash {

    @FXML
    private Rectangle recMain;

    @FXML
    private Rectangle recTemp;

    private MainApplication mainApp;

    public void setMainApp(MainApplication mainApplication) {
        this.mainApp = mainApplication;
    }

    public void initialize() {

        LoadingTask loadingTask = new LoadingTask();
        loadingTask.progressProperty().addListener((observable, oldValue, newValue) -> {

            recTemp.setWidth(recMain.getWidth() * newValue.doubleValue());

            if (newValue.doubleValue() >= 1.0) {
                try {

                    mainApp.showLoginClient();
                    mainApp.finishSplash();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        });

        new Thread(loadingTask).start();
    }

}
