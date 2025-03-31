package com.example.gui_movieapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveAdminController {
    private Stage stage;
    private Client client;

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Admin> adminTable;
    @FXML
    private TableColumn<Admin, String> nameColumn;
    @FXML
    private TableColumn<Admin, String> usernameColumn;
    @FXML
    private TableColumn<Admin, Void> deleteColumn;

    @FXML
    public void initialize() {
        client = Client.getInstance();

        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        usernameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));

        adminTable.getColumns().clear();
        adminTable.getColumns().addAll(nameColumn, usernameColumn, deleteColumn);
        adminTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addDeleteButton();

    }

    @FXML
    private void searchAdmins() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            adminTable.setItems(FXCollections.observableArrayList());
            return;
        }

        System.out.println(" Sending search request for: " + query);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        Map<String, Object> response = client.sendRequest("admin/search", query);

        System.out.println("Sent request: {query=" + query + "}");
        System.out.println("Response from server: " + response);

        if (response != null && response.containsKey("status") && "success".equalsIgnoreCase(response.get("status").toString())) {
            Map<String, Object> body = (Map<String, Object>) response.get("body");
            ObservableList<Admin> admins = FXCollections.observableArrayList();

            for (Map.Entry<String, Object> entry : body.entrySet()) {
                Map<String, Object> adminData = (Map<String, Object>) entry.getValue();
                String name = (String) adminData.get("name");
                String username = (String) adminData.get("username");

                Admin admin = new Admin(name, username, "");
                admins.add(admin);
            }

            adminTable.setItems(admins);
        } else {
            adminTable.setItems(FXCollections.observableArrayList());
            System.out.println(" No admins found.");
        }
    }


    private void addDeleteButton() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Admin admin = getTableView().getItems().get(getIndex());
                    removeAdmin(admin.getUsername());
                });
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void removeAdmin(String username) {
        Map<String, Object> response = client.sendRequest("admin/remove", username);
        if (response != null && response.containsKey("status") && "success".equalsIgnoreCase(response.get("status").toString())) {
            System.out.println("Admin removed: " + username);
            searchAdmins();
        } else {
            System.out.println("Failed to remove admin: " + username);
        }
    }

    @FXML
    private void goBackToAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/admin-view.fxml"));
        Parent root = loader.load();
        AdminController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("Admin Panel");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
