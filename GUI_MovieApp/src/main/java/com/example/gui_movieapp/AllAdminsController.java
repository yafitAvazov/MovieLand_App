package com.example.gui_movieapp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;
public class AllAdminsController {
    private Stage stage;
    private Client client;
    @FXML
    private TableView<Admin> adminsTable;
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

        addDeleteButton();
        adminsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadAdmins();
    }

    private void loadAdmins() {
        Map<String, Object> response = client.sendRequest("admin/getall");
        System.out.println("Response from server: " + response);

        ObservableList<Admin> admins = ResponseParser.getAllAdminsAsObjects(response);

        if (!admins.isEmpty()) {
            adminsTable.setItems(admins);
        } else {
            adminsTable.setPlaceholder(new Label("No admins available."));
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
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold; -fx-max-width: 40 ; -fx-alignment: center; -fx-font-size: 14px" );
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(deleteButton);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.setStyle("-fx-alignment: center; -fx-padding: 5px;");
                    setGraphic(hbox);
                }
            }
        });
    }

    private void removeAdmin(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the admin: " + username + "?");

        ButtonType confirmButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                Map<String, Object> serverResponse = client.sendRequest("admin/remove", username);
                if (serverResponse != null && "success".equalsIgnoreCase(serverResponse.get("status").toString())) {
                    System.out.println("Admin removed: " + username);
                    ObservableList<Admin> admins = adminsTable.getItems();
                    admins.removeIf(admin -> admin.getUsername().equals(username));
                    adminsTable.refresh();
                } else {
                    System.out.println("Failed to remove admin: " + username);
                }
            }
        });
    }

    @FXML
    private void goBackToSuperAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/superAdmin-view.fxml"));
        Parent root = loader.load();
        SuperAdminController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("Super-Admin Panel");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }





}
