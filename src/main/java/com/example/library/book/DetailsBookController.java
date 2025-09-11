package com.example.library.book;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import com.example.library.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.Rating;

import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DetailsBookController extends CoreDatabase {

    public DetailsBookController() {}

    private MainApplication mainApp = new MainApplication();

    @FXML
    private Label isbnLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label publisherLabel;
    @FXML
    private ImageView bookImage;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label labelIsbn;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelAuthor;
    @FXML
    private Label labelYear;
    @FXML
    private Label labelPublisher;
    @FXML
    private ScrollPane scrollPaneComment;
    @FXML
    private TextField textFieldComment;
    @FXML
    private Button buttonPostComment;
    @FXML
    private ListView<String> commentListView;
    @FXML
    private Rating rating;
    @FXML
    private ImageView qrCodeImageView;

    public static Book mainBook = new Book();

    private BookManager bookManager = new BookManager();

    private void loadImage(String imageUrl, ImageView imageView) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                Image image = new Image(input);
                imageView.setImage(image);
            } else {
                System.err.println("Error loading image: Server returned HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Errrrrror loading image: " + e.getMessage());
        }
    }

    static void loadQRCodeFromBlob(Blob qrCodeBlob, ImageView qrCodeImageView) throws SQLException {
        if (qrCodeBlob != null) {
            // Chuyển Blob thành InputStream
            InputStream inputStream = qrCodeBlob.getBinaryStream();

            // Tạo đối tượng Image từ InputStream
            Image qrCodeImage = new Image(inputStream);

            qrCodeImageView.setImage(qrCodeImage);
        } else {
            System.err.println("QR Code is null");
        }
    }


    public void setBookDetails(Book book) throws Exception {
        if (book == null) System.out.println("loi ko co sach");
        mainBook = book;
        loadImage(book.getImageUrlM(), bookImage);
        Blob qrCodeBlob = BookManager.getQrcodeBlob(book.getIsbn());
        loadQRCodeFromBlob(qrCodeBlob, qrCodeImageView);
        isbnLabel.setText(book.getIsbn());
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        yearLabel.setText(String.valueOf(book.getYearOfPublication()));
        publisherLabel.setText(book.getPublisher());
    }

    public void initialize() {
        loadComment();
    }

    @FXML
    private void borrowBook() throws Exception {
        mainApp.showBookBorrow();
    }

    @FXML
    private void returnToMenu() throws Exception {
        mainApp.showSearchView();
    }


    private void updateBookRatingInDatabase(double ratingValue) {
        String sql = "UPDATE books SET rating = ? WHERE isbn = ?";
        con = AccountDB.getInstance().getConnection();
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setDouble(1, ratingValue);
            pstm.setString(2, mainBook.getIsbn());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addComment() {

        String sql = "INSERT INTO book_comments (book_id, user_id, content) "
                + "VALUES (?, ?, ?)";
        con = AccountDB.getInstance().getConnection();
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, mainBook.getIsbn());
            pstm.setInt(2, AccountDB.getId());
            pstm.setString(3, textFieldComment.getText());

            int test = pstm.executeUpdate();
            if (test > 0) {
                System.out.println("co");
            } else {
                System.out.println("sai o excute");
            }
            initialize();
            textFieldComment.clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadComment() {

        List<Comment> commentList = Comment.getCommentList(AccountDB.getIsbn());

        if (commentList == null || commentList.isEmpty()) {
            ObservableList<String> noComments = FXCollections.observableArrayList("No comments available.");
            commentListView.setItems(noComments);
            return;
        }

        ObservableList<String> comments = FXCollections.observableArrayList();

        for (Comment comment : commentList) {
            String displayText = "User: " + comment.getUserId() + "\n" + comment.getContent();
            comments.add(displayText);
        }

        commentListView.setItems(comments);

        commentListView.setStyle("-fx-font-size: 14px; -fx-wrap-text: true;");

    }

    public void handleMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        double selectedRating = rating.getRating();
        System.out.println("User rating: " + selectedRating);

        updateBookRatingInDatabase(selectedRating);
    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }
}
