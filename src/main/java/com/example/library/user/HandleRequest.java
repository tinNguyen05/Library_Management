package com.example.library.user;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import com.example.library.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleRequest extends CoreDatabase {
    private MainApplication mainApp = new MainApplication();

    @FXML
    private TableView<Request> tableView;

    @FXML
    private TableColumn<Request, Integer> idColumn;

    @FXML
    private TableColumn<Request, String> bookIdColumn;

    @FXML
    private TableColumn<Request, Integer> userIdColumn;

    @FXML
    private TableColumn<Request, String> requestDateColumn;

    @FXML
    private TableColumn<Request, String> statusColumn;

    @FXML
    private Button approveButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Text statusMessage;

    @FXML
    private Button btnBack;

    private ObservableList<Request> requestList;

    public void initialize() {
        // Khởi tạo danh sách yêu cầu
        requestList = FXCollections.observableArrayList();

        // Liên kết các cột với thuộc tính của đối tượng Request
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Tải dữ liệu từ database
        loadRequestData();

        // Thiết lập hành động cho nút Duyệt
        approveButton.setOnAction(event -> handleApproveRequest());

        // Thiết lập hành động cho nút Từ Chối
        rejectButton.setOnAction(event -> handleRejectRequest());
    }

    // Hàm để tải dữ liệu từ database
    private void loadRequestData() {
        String query = "SELECT * FROM request_borrow_book WHERE status = 'pending'";
        try (Connection conn = AccountDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            requestList.clear(); // Đảm bảo bảng không bị lặp lại dữ liệu cũ
            while (rs.next()) {
                int id = rs.getInt("id");
                String bookId = rs.getString("book_id");
                int userId = rs.getInt("user_id");
                String requestDate = rs.getString("request_date");
                String status = rs.getString("status");

                Request request = new Request(id, bookId, userId, requestDate, status);
                requestList.add(request);
            }
        } catch (SQLException e) {
            showError("Lỗi tải dữ liệu yêu cầu mượn sách", e.getMessage());
        }

        // Cập nhật TableView
        tableView.setItems(requestList);
    }

    // Hàm xử lý duyệt yêu cầu mượn sách
    @FXML
    private void handleApproveRequest() {
        Request selectedRequest = tableView.getSelectionModel().getSelectedItem();

        if (selectedRequest == null) {
            showError("Chưa chọn yêu cầu", "Vui lòng chọn yêu cầu mượn sách để duyệt.");
            return;
        }

        String updateQuery = "UPDATE request_borrow_book SET status = 'approved' WHERE id = ?";
        String insertBookBorrow = "INSERT INTO book_borrowed (book_id, user_id, borrow_date, return_date, nums) VALUES (?, ?, NOW(), ?, ?)";
        String inforQuery = "SELECT nums FROM request_borrow_book WHERE book_id = ?";

        try (Connection conn = AccountDB.getInstance().getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertBookBorrow);
             PreparedStatement infoStmt = conn.prepareStatement(inforQuery)) {

            // 1. Lấy thông tin số lượng sách từ bảng `books`
            infoStmt.setString(1, selectedRequest.getBookId());
            ResultSet rs = infoStmt.executeQuery();

            if (!rs.next()) {
                showError("Lỗi", "Không tìm thấy thông tin sách.");
                return;
            }

            int nums = rs.getInt("nums");
            if (nums <= 0) {
                showError("Lỗi", "Sách đã hết, không thể duyệt yêu cầu.");
                return;
            }

            updateStmt.setInt(1, selectedRequest.getId());
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                insertStmt.setString(1, selectedRequest.getBookId());
                insertStmt.setInt(2, selectedRequest.getUserId());
                insertStmt.setString(3, selectedRequest.getRequestDate());
                insertStmt.setInt(4, nums);

                int insertRows = insertStmt.executeUpdate();

                if (insertRows > 0) {
                    selectedRequest.setStatus("approved");
                    statusMessage.setText("Yêu cầu mượn sách đã được duyệt.");
                    statusMessage.setStyle("-fx-fill: green;");
                    loadRequestData(); // Tải lại dữ liệu để đồng bộ với cơ sở dữ liệu
                } else {
                    showError("Lỗi", "Không thể thêm thông tin vào bảng book_borrowed.");
                }
            } else {
                showError("Lỗi duyệt yêu cầu", "Không thể duyệt yêu cầu mượn sách này.");
            }
        } catch (SQLException e) {
            showError("Lỗi duyệt yêu cầu", e.getMessage());
        }
    }


    // Hàm xử lý từ chối yêu cầu mượn sách
    @FXML
    private void handleRejectRequest() {
        Request selectedRequest = tableView.getSelectionModel().getSelectedItem();

        if (selectedRequest == null) {
            showError("Chưa chọn yêu cầu", "Vui lòng chọn yêu cầu mượn sách để từ chối.");
            return;
        }

        String updateQuery = "UPDATE request_borrow_book SET status = 'rejected' WHERE id = ?";
        try (Connection conn = AccountDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, selectedRequest.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                selectedRequest.setStatus("rejected");
                statusMessage.setText("Yêu cầu mượn sách đã bị từ chối.");
                statusMessage.setStyle("-fx-fill: red;");
                loadRequestData(); // Tải lại dữ liệu để đồng bộ với cơ sở dữ liệu
            } else {
                showError("Lỗi từ chối yêu cầu", "Không thể từ chối yêu cầu mượn sách này.");
            }
        } catch (SQLException e) {
            showError("Lỗi từ chối yêu cầu", e.getMessage());
        }
    }

    // Hàm hiển thị thông báo lỗi
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openMainMenu() throws Exception {
        mainApp.showUser();
    }

    public void setMainApp(MainApplication mainApplication) {
        mainApp = mainApplication;
    }
}
