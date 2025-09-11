package com.example.library.book;

import com.example.library.AccountDB;
import com.example.library.MainApplication;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LibrarySearchController {

    private MainApplication mainApp = new MainApplication();


    @FXML
    private TextField searchField;
    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;
    @FXML
    private ImageView imageView5;
    @FXML
    private ImageView imageView6;
    @FXML
    private ImageView imageView7;
    @FXML
    private ImageView imageView8;
    @FXML
    private ImageView imageViewTop;

    @FXML
    private Label bookName;

    @FXML
    private ListView<String> suggestionListView;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnNext;

    private int currPage = 0;
    private List<Book> currBookList = new ArrayList<>();
    private final BookManager bookManager = new BookManager();

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final int BOOKS_PER_PAGE = 8;

    public void loadImage(final String imageUrl, final ImageView imageView) {
        Task<Image> loadImageTask = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                java.net.URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    Image image = new Image(input);

                    if (image.getHeight() < 50 || image.getWidth() < 50) {
                        System.out.println("Using coverError image because the size is too small: " + image.getHeight() + "x" + image.getWidth());
                        String imagePath = getClass().getResource("/com/example/library/image/errorImage.jpg").toExternalForm();
                        image = new Image(imagePath);
                    }

                    return image;
                } else {
                    throw new Exception("Error loading image: Server returned HTTP response code: " + responseCode + " for URL: " + imageUrl);
                }
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            imageView.setImage(image);
        });

        loadImageTask.setOnFailed(event -> {
            Throwable exception = loadImageTask.getException();
            System.err.println("Error loading image: " + exception.getMessage());
        });

        new Thread(loadImageTask).start();
    }

    private void updateSuggestions(String query) {
        List<String> allBookTitles = bookManager.getAllBooksTitle();

        List<String> filteredSuggestions = allBookTitles.stream()
                .filter(title -> title.toLowerCase().contains(query.toLowerCase()))
                .toList();

        suggestionListView.getItems().setAll(filteredSuggestions);

        suggestionListView.setVisible(!filteredSuggestions.isEmpty());
    }


    @FXML
    public void initialize() {

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBlank()) {
                suggestionListView.setVisible(false);
            } else {
                updateSuggestions(newValue);
            }
        });

        suggestionListView.setOnMouseClicked(event -> {
            String selectedSuggestion = suggestionListView.getSelectionModel().getSelectedItem();
            if (selectedSuggestion != null) {
                searchField.setText(selectedSuggestion);
                suggestionListView.setVisible(false);
                searchBook(selectedSuggestion);

            }
        });

        Book highestRatedBook = bookManager.getHighestRatedBook();
        if (highestRatedBook != null) {
            setupBookDisplay(highestRatedBook, imageViewTop);
            bookName.setText(highestRatedBook.getTitle());
        }
    }


    @FXML
    private void clickSearchButton(ActionEvent e) throws Exception {
        String keyWord = searchField.getText();
        Task<Void> searchTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                searchBook(keyWord);
                return null;
            }

            @Override
            protected void succeeded() {
                suggestionListView.setVisible(false);
            }

            @Override
            protected void failed() {
                System.out.println("Tìm kiếm thất bại");
            }
        };

        new Thread(searchTask).start();
    }


    private void loadBooksForPage(int page) {
        Task<List<Book>> loadBooksTask = new Task<>() {
            @Override
            protected List<Book> call() throws Exception {
                List<Book> bookList = bookManager.searchBooks(searchField.getText());
                int startIndex = page * 8;
                int endIndex = Math.min(startIndex + 8, bookList.size());
                return bookList.subList(startIndex, endIndex);
            }
        };

        loadBooksTask.setOnSucceeded(event -> {
            try {
                List<Book> bookList = loadBooksTask.getValue();
                currBookList = bookList;

                setupBookDisplay(currBookList.get(0), imageView1);
                setupBookDisplay(currBookList.get(1), imageView2);
                setupBookDisplay(currBookList.get(2), imageView3);
                setupBookDisplay(currBookList.get(3), imageView4);
                setupBookDisplay(currBookList.get(4), imageView5);
                setupBookDisplay(currBookList.get(5), imageView6);
                setupBookDisplay(currBookList.get(6), imageView7);
                setupBookDisplay(currBookList.get(7), imageView8);

                btnBack.setVisible(page > 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadBooksTask.setOnFailed(event -> {
            System.err.println("Load books failed: " + loadBooksTask.getException().getMessage());
        });

        new Thread(loadBooksTask).start();
    }


    @FXML
    private void loadNextBooks() throws Exception {
        currPage++;
        loadBooksForPage(currPage);
    }

    @FXML
    private void loadPreviousBooks() throws Exception {
        if (currPage > 0) {
            currPage--;
            loadBooksForPage(currPage);
        }
    }


    private ImageView getImageViewForIndex(int index) {
        return switch (index) {
            case 0 -> imageView1;
            case 1 -> imageView2;
            case 2 -> imageView3;
            case 3 -> imageView4;
            case 4 -> imageView5;
            case 5 -> imageView6;
            case 6 -> imageView7;
            case 7 -> imageView8;
            default -> null;
        };
    }

    private void searchBook(String keyWord) {
        Task<List<Book>> searchTask = new Task<>() {
            @Override
            protected List<Book> call() throws Exception {
                return bookManager.searchBooks(keyWord);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<Book> bookList = searchTask.getValue();
            if (!bookList.isEmpty()) {
                Book topRatedBook = bookList.get(0);
                setupBookDisplay(topRatedBook, imageViewTop);

                bookName.setText(topRatedBook.getTitle());

                for (int i = 0; i < BOOKS_PER_PAGE; i++) {
                    ImageView imageView = getImageViewForIndex(i);
                    if (i < bookList.size()) {
                        setupBookDisplay(bookList.get(i), imageView);
                    } else {
                        imageView.setImage(null);
                    }
                }


                btnNext.setDisable(bookList.size() < BOOKS_PER_PAGE);
            } else {
                clearBookDisplays();
                bookName.setText("");
            }
        });

        searchTask.setOnFailed(event -> logError("Search failed", searchTask.getException()));

        executorService.submit(searchTask);
    }


    private void logError(String message, Throwable throwable) {
        System.err.println(message + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }

    private void clearBookDisplays() {
        for (int i = 0; i < BOOKS_PER_PAGE; i++) {
            ImageView imageView = getImageViewForIndex(i);
            if (imageView != null) {
                imageView.setImage(null);
            }
        }
        imageViewTop.setImage(null);
    }

    private void setupBookDisplay(Book book, ImageView imageView) {
        if (book == null) {
            imageView.setImage(null);
            return;
        }

        loadImage(book.getImageUrlM(), imageView);

        imageView.setOnMouseClicked(event -> {
            try {
                if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
                    throw new IllegalArgumentException("Invalid ISBN");
                }

                bookName.setText(book.getTitle());

                AccountDB.setIsbn(book.getIsbn());
                mainApp.showDetailBook(book);
            } catch (Exception e) {
                logError("Error showing book details", e);
            }
        });
    }

    public void setMainApplication(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void returnToMainMenu() throws Exception {
        mainApp.showMainMenu();
    }
}