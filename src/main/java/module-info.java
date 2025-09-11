module com.example.library {
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires javafx.media;
    requires jdk.httpserver;


    opens com.example.library to javafx.fxml;
    exports com.example.library;
    exports com.example.library.book;
    opens com.example.library.book to javafx.fxml;
    exports com.example.library.user;
    opens com.example.library.user to javafx.fxml;
}