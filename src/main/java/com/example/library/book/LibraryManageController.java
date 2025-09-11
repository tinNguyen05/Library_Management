package com.example.library.book;

import com.example.library.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;

public class LibraryManageController {

    private MainApplication mainApp;

    @FXML
    private TextField isbnField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField publisherField;
    @FXML
    private Label statusLabel;

    private final BookManager bookManager = new BookManager();

    @FXML
    private void addBook() {
        try {
            Book book = new Book(
                    isbnField.getText(),
                    titleField.getText(),
                    authorField.getText(),
                    Integer.parseInt(yearField.getText()),
                    publisherField.getText(),
                    "", "", ""
            );
            bookManager.addBook(book);
            statusLabel.setText("Sách đã được thêm thành công.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Lỗi: Thông tin sách không hợp lệ.");
        }
    }

    @FXML
    private void updateBook() {
        try {
            Book book = new Book(
                    isbnField.getText(),
                    titleField.getText(),
                    authorField.getText(),
                    Integer.parseInt(yearField.getText()),
                    publisherField.getText(),
                    "", "", ""
            );
            bookManager.updateBook(book);
            statusLabel.setText("Sách đã được cập nhật thành công.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Lỗi: Thông tin sách không hợp lệ.");
        }
    }

    @FXML
    private void deleteBook() {
        String isbn = isbnField.getText();
        String tmp = bookManager.deleteBook(isbn);
        statusLabel.setText(tmp);
    }

    public void setMainApplication(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void returnToMainMenu() throws Exception {
        mainApp.showUser();
    }

}
