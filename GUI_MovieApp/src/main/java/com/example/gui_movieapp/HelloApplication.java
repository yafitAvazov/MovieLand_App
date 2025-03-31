package com.example.gui_movieapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gui_movieapp/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 700);
        HelloController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("MovieLand");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}