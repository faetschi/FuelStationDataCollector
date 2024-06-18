package at.fhtw.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;


public class HelloController {

    @FXML
    private TextField customerIdField;
    @FXML
    private Label statusLabel;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String API_URL = "http://localhost:8080/invoices/";

    @FXML
    protected void generateInvoice() {
        String customerId = customerIdField.getText();
        if (!customerId.isEmpty()) {
            sendStartInvoiceRequest(customerId);
        } else {
            statusLabel.setText("Customer ID cannot be empty.");
        }
    }

    private void sendStartInvoiceRequest(String customerId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + customerId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        httpClient.sendAsync(request, BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        statusLabel.setText("Invoice generation started for Customer ID: " + customerId);
                        startPeriodicCheck(customerId);
                    } else {
                        statusLabel.setText("Failed to start invoice generation.");
                    }
                })
                .exceptionally(e -> {
                    statusLabel.setText("Error: " + e.getMessage());
                    return null;
                });
    }

    private void startPeriodicCheck(String customerId) {
        Task<Void> checkTask = new Task<>() {
            protected Void call() throws Exception {
                while (true) {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(API_URL + customerId))
                            .GET()
                            .build();

                    HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
                    if (response.statusCode() == 200) {
                        updateMessage("Invoice ready: " + response.body());
                        break;
                    }
                    Thread.sleep(5000); // Check every 5 seconds
                }
                return null;
            }
        };
        statusLabel.textProperty().bind(checkTask.messageProperty());
        new Thread(checkTask).start();
    }
}

