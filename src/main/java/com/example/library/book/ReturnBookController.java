package com.example.library.book;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReturnBookController extends CoreDatabase {

    @FXML
    private Button btnReturn;

    @FXML
    private TableColumn<?, ?> colIdBook;

    @FXML
    private TableColumn<?, ?> colNums;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private AnchorPane returnView;


    public void initialize() {
        colIdBook.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colNums.setCellValueFactory(new PropertyValueFactory<>("nums"));

        loadData();
    }

    private void loadData() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String sql = "SELECT * FROM book_borrowed WHERE user_id = ?";

        try {
            st = con.prepareStatement(sql);
            st.setInt(1, AccountDB.getId());
            rs = st.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("book_id");
                int nums = rs.getInt("nums");
                books.add(new Book(isbn, nums));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(books);
    }

    @FXML
    private void returnBook() {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Chưa chọn sách để trả!");
            alert.setContentText("Hãy chọn sách cần trả từ danh sách.");
            alert.showAndWait();
            return;
        }

        String updateBookStockSql = "UPDATE books SET nums = nums + ? WHERE ISBN = ?";
        String deleteBorrowRecordSql = "DELETE FROM request_borrow_book WHERE book_id = ? AND user_id = ?";
        String deleteBorrow = "DELETE FROM book_borrowed WHERE book_id = ? AND user_id = ?";

        try (PreparedStatement updatePstm = con.prepareStatement(updateBookStockSql);
             PreparedStatement deletePstm = con.prepareStatement(deleteBorrowRecordSql);
             PreparedStatement deleteBorrowPstm = con.prepareStatement(deleteBorrow)) {

            int numsReturned = selectedBook.getNums();
            updatePstm.setInt(1, numsReturned);
            updatePstm.setString(2, selectedBook.getIsbn());
            int rowsAffected = updatePstm.executeUpdate();

            if (rowsAffected > 0) {
                deletePstm.setString(1, selectedBook.getIsbn());
                deletePstm.setInt(2, AccountDB.getId());
                deletePstm.executeUpdate();

                deleteBorrowPstm.setString(1, selectedBook.getIsbn());
                deleteBorrowPstm.setInt(2, AccountDB.getId());
                deleteBorrowPstm.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Trả sách thành công!");
                alert.setContentText("Sách đã được trả thành công và kho đã được cập nhật.");
                alert.showAndWait();

                tableView.getItems().remove(selectedBook);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Trả sách không thành công!");
                alert.setContentText("Có lỗi xảy ra trong quá trình trả sách.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Trả sách không thành công!");
            alert.setContentText("Lỗi: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void close() {
        ((Stage) returnView.getScene().getWindow()).close();
    }
}
