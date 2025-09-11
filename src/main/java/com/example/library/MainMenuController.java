package com.example.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends CoreDatabase implements Initializable {
    private MainApplication mainApp;

    @FXML
    private Button btnReturn;

    @FXML
    private BarChart<?, ?> bookBorrowChart;

    @FXML
    private javafx.scene.control.Label totalBook;

    @FXML
    private javafx.scene.control.Label totalBorrowBook;

    @FXML
    private Button offMusic;

    @FXML
    private Button onMusic;

    @FXML
    private javafx.scene.control.Label nameUser;

    @FXML
    private javafx.scene.control.Button toggleBackgroundMusicButton;

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void openSearch() throws Exception {
        mainApp.showSearchView();
    }

    @FXML
    private void openLogin() throws Exception {
        mainApp.showLoginClient();
    }

    public void displayTotalBook() {
        String sql = "SELECT COUNT(ISBN) FROM books";
        con = AccountDB.getInstance().getConnection();
        int count = 0;

        try {
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            totalBook.setText(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayTotalBookBorrowed() {
        String sql = "SELECT COUNT(id) FROM request_borrow_book WHERE status = 'approved' ";
        con = AccountDB.getInstance().getConnection();

        int count2 = 0;

        try {
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                count2 = rs.getInt(1);
            }
            totalBorrowBook.setText(String.valueOf(count2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayBookBorrowedChart() {
        String sql = "SELECT book_id, sum(nums) FROM book_borrowed group by book_id";

        bookBorrowChart.getData().clear();
        con = AccountDB.getInstance().getConnection();

        try {
            XYChart.Series chartSeries = new XYChart.Series();

            st = con.prepareStatement(sql);
            rs = st.executeQuery();

            while(rs.next()) {
                chartSeries.getData().add(new XYChart.Data(rs.getString(1), rs.getInt(2)));
            }

            bookBorrowChart.getData().add(chartSeries);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void displayUserName() {
        int currentUsername = AccountDB.id;

        String sql = "SELECT name FROM userdata WHERE id = ?";
        con = AccountDB.getInstance().getConnection();

        try {
            st = con.prepareStatement(sql);
            st.setInt(1, currentUsername);
            rs = st.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("name");
                nameUser.setText(fullName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initialize(URL location, ResourceBundle resources) {
        displayTotalBook();
        displayTotalBookBorrowed();
        displayBookBorrowedChart();
        displayUserName();
    }

    @FXML
    private void handleToggleBackgroundMusic() {
        MainApplication.toggleBackgroundMusic(); // Gọi phương thức từ Main để dừng/phát nhạc nền

        if(MainApplication.isBackgroundMusicPlaying()) {
            onMusic.setVisible(true);
            offMusic.setVisible(false);
        } else {
            onMusic.setVisible(false);
            offMusic.setVisible(true);
        }

    }

    public void showReturnBookView() throws Exception {
        mainApp.showBookReturn();
    }

    public void showChangePassView() throws Exception {
        mainApp.showChangePass();
    }

}
