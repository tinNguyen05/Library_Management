package com.example.library.user;

public class User {
    private String username;
    private String password;
    private String name;
    private String role;
    private String phone;
    private String classname;
    private int id;


    public User(String username, String password, String name, String phone, String classname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.classname = classname;
    }

    public User(String username, String password, String name, String role, String phone, String classname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.classname = classname;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getClassname() {
        return classname;
    }
}
