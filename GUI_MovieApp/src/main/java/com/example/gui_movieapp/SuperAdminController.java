package com.example.gui_movieapp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuperAdminController {
    private Stage stage;
    private Client client;
    private String loggedInUser;


    @FXML
    public void initialize() {
        client = Client.getInstance();
    }


    public void setLoggedInAdmin(String username) {
        this.loggedInUser = username;
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

    @FXML
    private void AddAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/add_admins.fxml"));
        Parent root = loader.load();
        AddAdminController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("MovieLand - Add Admin");
    }

    @FXML
    private void AllAdmins() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/all_admins.fxml"));
        Parent root = loader.load();
        AllAdminsController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("MovieLand - All Admins");
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}


