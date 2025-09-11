package com.example.library.book;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int yearOfPublication;
    private String publisher;
    private String imageUrlS;
    private String imageUrlM;
    private String imageUrlL;
    private int nums;

    public Book(String isbn, int nums) {
        this.isbn = isbn;
        this.nums = nums;
    }

    public Book(String isbn, String title, String author, int yearOfPublication, String publisher, String imageUrlS, String imageUrlM, String imageUrlL) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.publisher = publisher;
        this.imageUrlS = imageUrlS;
        this.imageUrlM = imageUrlM;
        this.imageUrlL = imageUrlL;
    }

    public Book(String isbn, String title, String author, int yearOfPublication, String publisher, String imageUrlS, String imageUrlM, String imageUrlL, int nums) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.publisher = publisher;
        this.imageUrlS = imageUrlS;
        this.imageUrlM = imageUrlM;
        this.imageUrlL = imageUrlL;
        this.nums = nums;
    }

    public Book() {}

    // Getters and Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrlS() {
        return imageUrlS;
    }

    public void setImageUrlS(String imageUrlS) {
        this.imageUrlS = imageUrlS;
    }

    public String getImageUrlM() {
        return imageUrlM;
    }

    public void setImageUrlM(String imageUrlM) {
        this.imageUrlM = imageUrlM;
    }

    public String getImageUrlL() {
        return imageUrlL;
    }

    public void setImageUrlL(String imageUrlL) {
        this.imageUrlL = imageUrlL;
    }

    public int getNums() {
        return nums;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + isbn + '\'' +
                ", Title='" + title + '\'' +
                ", Author='" + author + '\'' +
                ", Year Of Publication=" + yearOfPublication +
                ", Publisher='" + publisher + '\'' +
                ", Image URL S='" + imageUrlS + '\'' +
                ", Image URL M='" + imageUrlM + '\'' +
                ", Image URL L='" + imageUrlL + '\'' +
                '}';
    }
}
