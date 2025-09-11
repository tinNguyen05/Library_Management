package com.example.library.book;


import com.example.library.AccountDB;
import com.example.library.CoreDatabase;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookManager extends CoreDatabase {

    public void addBook(Book book) {
        String sql = "INSERT INTO books (ISBN, `Title`, `Author`, `PublishYear`, Publisher, `Image-URL-S`, `Image-URL-M`, `Image-URL-L`, qr_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getYearOfPublication());
            pstmt.setString(5, book.getPublisher());
            pstmt.setString(6, book.getImageUrlS());
            pstmt.setString(7, book.getImageUrlM());
            pstmt.setString(8, book.getImageUrlL());

            // Tạo dữ liệu QR Code từ các thông tin sách
            // Bước 1: Tạo URL tìm kiếm Google từ tên sách, tác giả và nhà xuất bản
            String searchQuery = book.getTitle() + " " + book.getAuthor() + " " + book.getPublisher();
            String searchUrl = "https://www.google.com/search?q=" + searchQuery.replace(" ", "+");

            // Tạo mã QR từ chuỗi qrData và lưu vào database
            byte[] qrCodeBytes = QRCodeGenerator.generateQRCodeBytes(searchUrl);
            pstmt.setBytes(9, qrCodeBytes); // Lưu ảnh QR dưới dạng BLOB

            pstmt.executeUpdate();
            System.out.println("Thêm sách thành công!");
        } catch (SQLException | WriterException | IOException e) {
            System.err.println("Lỗi khi thêm sách: " + e.getMessage());
        }
    }

    public List<String> getAllBooksTitle() {
        String sql = "SELECT * FROM books";
        List<String> bookTitles = new ArrayList<>();
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con; Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tmp = rs.getString("Title");
                bookTitles.add(tmp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sách: " + e.getMessage());
        }
        return bookTitles;
    }

    public Book getHighestRatedBook() {
        String sql = "SELECT * FROM books ORDER BY rating DESC LIMIT 1"; // Sắp xếp theo rating giảm dần và lấy 1 sách
        Book highestRatedBook = null;
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con; Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                highestRatedBook = new Book(
                        rs.getString("ISBN"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getInt("PublishYear"),
                        rs.getString("Publisher"),
                        rs.getString("Image-URL-S"),
                        rs.getString("Image-URL-M"),
                        rs.getString("Image-URL-L")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sách đánh giá cao nhất: " + e.getMessage());
        }
        return highestRatedBook;
    }

    public List<Book> searchBooks(String keyword) {
        String sql = "SELECT * FROM books WHERE `Title` LIKE ? OR `Author` LIKE ?";
        List<Book> searchResults = new ArrayList<>();
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("ISBN"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getInt("PublishYear"),
                        rs.getString("Publisher"),
                        rs.getString("Image-URL-S"),
                        rs.getString("Image-URL-M"),
                        rs.getString("Image-URL-L")
                );
                searchResults.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm sách: " + e.getMessage());
        }
        return searchResults;
    }

    public Book searchBookIsbn(String tmpIsbn) throws SQLException {
        String sql = "SELECT * FROM books WHERE `ISBN` = ?";
        Book book = null;
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con; PreparedStatement pstm = conn.prepareStatement(sql)) {
            // Đặt giá trị ISBN vào PreparedStatement
            pstm.setString(1, tmpIsbn);
            try (ResultSet rs = pstm.executeQuery()) {
                // Kiểm tra nếu có dữ liệu trong ResultSet
                if (rs.next()) {
                    book = new Book();
                    book.setIsbn(rs.getString("ISBN"));
                    book.setTitle(rs.getString("Title"));
                    book.setAuthor(rs.getString("Author"));
                    book.setYearOfPublication(rs.getInt("PublishYear"));
                    book.setPublisher(rs.getString("Publisher"));
                    book.setImageUrlS(rs.getString("Image-Url-S"));
                    book.setImageUrlM(rs.getString("Image-Url-M"));
                    book.setImageUrlL(rs.getString("Image-Url-L"));
                } else {
                    System.err.println("No book found with ISBN: " + tmpIsbn);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchBookIsbn: " + e.getMessage());
            throw e;
        }

        return book;
    }


    // Cập nhật sách trong cơ sở dữ liệu
    public void updateBook(Book book) {
        String sql = "UPDATE books SET `Title` = ?, `Author` = ?, `PublishYear` = ?, Publisher = ?, `Image-URL-S` = ?, `Image-URL-M` = ?, `Image-URL-L` = ? WHERE ISBN = ?";
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYearOfPublication());
            pstmt.setString(4, book.getPublisher());
            pstmt.setString(5, book.getImageUrlS());
            pstmt.setString(6, book.getImageUrlM());
            pstmt.setString(7, book.getImageUrlL());
            pstmt.setString(8, book.getIsbn());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Cập nhật sách thành công!");
            } else {
                System.out.println("Không tìm thấy sách với ISBN: " + book.getIsbn());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sách: " + e.getMessage());
        }
    }

    public String deleteBook(String isbn) {
        String sql = "DELETE FROM books WHERE ISBN = ?";
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
//                System.out.println("Xóa sách thành công!");
                return "Xóa sách thành công!";
            } else {
//                System.out.println("Không tìm thấy sách với ISBN: " + isbn);
                return "Không tìm thấy sách";
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sách: " + e.getMessage());
        }
        return "Lỗi xóa sách không thành công";
    }

    public void PostCommentForBook(String cmt, String ISBN) {}

    public static Blob getQrcodeBlob(String isbn) {
        String sql = "SELECT qr_code FROM books WHERE ISBN = ?";
        Blob qrCodeBlob = null;
        con = AccountDB.getInstance().getConnection();
        try (Connection conn = con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    qrCodeBlob = rs.getBlob("qr_code");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching QR code Blob: " + e.getMessage());
        }
        return qrCodeBlob;
    }

}
