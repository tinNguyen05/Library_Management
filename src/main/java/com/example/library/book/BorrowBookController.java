package com.example.library.book;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;

public class BorrowBookController extends CoreDatabase {


    @FXML
    private TextField tNums;

    @FXML
    private DatePicker tReturnD;

    Book mainBook =  DetailsBookController.mainBook;

    @FXML
    private void borrowBook() {
        String sql = "INSERT INTO request_borrow_book (book_id, user_id, request_date, status, nums) "
                + "VALUES (?, ?, ?, ?, ?)";
        con = AccountDB.getInstance().getConnection();

        try (PreparedStatement checkStockPstm = con.prepareStatement("SELECT nums FROM books WHERE ISBN = ?")) {
            checkStockPstm.setString(1, mainBook.getIsbn());
            rs = checkStockPstm.executeQuery();

            if (rs.next()) {
                int availableStock = rs.getInt("nums");

                int numsToBorrow = Integer.parseInt(tNums.getText());

                if (numsToBorrow > availableStock) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Số sách mượn vượt quá số sách trong kho!");
                    alert.setContentText("Chỉ còn " + availableStock + " sách trong kho.");
                    alert.showAndWait();
                    return;
                }
                Date returnDate = Date.valueOf(tReturnD.getValue());
                LocalDate nowDate = LocalDate.now();
                Date currDate = Date.valueOf(nowDate);

                if (returnDate.before(currDate)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Ngày trả sách không hợp lệ!");
                    alert.setContentText("Ngày trả sách không thể trước ngày mượn!");
                    alert.showAndWait();
                    return;
                }

                try (PreparedStatement pstm = con.prepareStatement(sql)) {
                    pstm.setString(1, mainBook.getIsbn());
                    pstm.setInt(2, AccountDB.getId());


//                    pstm.setDate(3, borrowDate);
                    pstm.setDate(3, returnDate);

                    pstm.setString(4, "pending");

                    pstm.setInt(5, numsToBorrow);

                    int rowsAffected = pstm.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    if (rowsAffected > 0) {
                        String updateBookSql = "UPDATE books SET nums = nums - ? WHERE ISBN = ?";
                        try (PreparedStatement updatePstm = con.prepareStatement(updateBookSql)) {
                            updatePstm.setInt(1, numsToBorrow);
                            updatePstm.setString(2, mainBook.getIsbn());
                            updatePstm.executeUpdate();
                        }

                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Mượn sách thành công!");
                        alert.setContentText("Bạn đã mượn sách thành công.");

                        clear();

                    } else {
                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Mượn sách không thành công!");
                        alert.setContentText("Có lỗi xảy ra trong quá trình mượn sách.");
                    }

                    alert.showAndWait();

                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Mượn sách không thành công!");
                    alert.setContentText("Lỗi: " + e.getMessage());
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Sách không tồn tại!");
                alert.setContentText("Không tìm thấy sách với ISBN: " + mainBook.getIsbn());
                alert.showAndWait();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Mượn sách không thành công!");
            alert.setContentText("Lỗi: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void clear() {
//        tBorrowD.setValue(null);
        tNums.setText("");
        tReturnD.setValue(null);
    }

}
