package com.example.library;

import com.example.library.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccountDB {

    // Các thuộc tính tĩnh
    public static int id;
    public static String ISBN;

    // Phương thức getter cho id
    public static int getId() {
        return id;
    }

    // Phương thức setter cho id
    public static void setId(int id) {
        AccountDB.id = id;
    }

    // Phương thức getter cho ISBN
    public static String getIsbn() {
        return ISBN;
    }

    // Phương thức setter cho ISBN
    public static void setIsbn(String isbn) {
        AccountDB.ISBN = isbn;
    }
    // Các thông số kết nối
    private static final String USER = "root";
    private static final String PASSWORD = "21012005";
    private static final String URL = "jdbc:mysql://localhost/book";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // Đối tượng duy nhất của AccountDB
    private static AccountDB instance;
    private Connection connection;

    // Tránh tạo đối tượng mới từ bên ngoài (private constructor)
    private AccountDB() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error while initializing database connection", e);
        }
    }

    // Phương thức để lấy đối tượng duy nhất (Singleton instance)
    public static AccountDB getInstance() {
        if (instance == null) {
            synchronized (AccountDB.class) {
                if (instance == null) {
                    instance = new AccountDB();
                }
            }
        }
        return instance;
    }

    // Phương thức để lấy kết nối
    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

}
