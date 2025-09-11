package com.example.library;

import com.example.library.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CoreDatabase {
    protected static Connection con;
    protected PreparedStatement st;
    protected ResultSet rs;
    protected User mainUser;

    protected void openConnection() throws SQLException {
        con = AccountDB.getInstance().getConnection();
    }

}
