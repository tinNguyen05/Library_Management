package com.example.library.book;

import com.example.library.AccountDB;
import com.example.library.CoreDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Comment extends CoreDatabase {
    private int id;
    private String book_id;
    private int user_id;
    private String content;
    private String date;

    public Comment (int id, String book_id, int user_id, String content, String date) {
        this.id = id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getBook_id() {
        return this.book_id;
    }

    public int getUserId() {
        return this.user_id;
    }

    public String getContent() {
        return this.content;
    }

    public String getDate() {
        return this.date;
    }

    public static List<Comment> getCommentList(String ISBN) {
        List<Comment> commentList = new ArrayList<>();
        con = AccountDB.getInstance().getConnection();


        String sql = "SELECT * FROM book_comments WHERE book_id = ?";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, ISBN);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Comment cmt = new Comment(
                        rs.getInt("id"),
                        rs.getString("book_id"),
                        rs.getInt("user_id"),
                        rs.getString("content"),
                        rs.getString("created_at")
                );
                commentList.add(cmt);
            }
        } catch (Exception e) {
            System.out.println("sai khi khoi tao cmt :" + e.getMessage());
            e.printStackTrace();
        }
        return commentList;
    }
}
