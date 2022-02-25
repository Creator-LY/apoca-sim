package com.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    private Image icon;
    private Scene scene;
    private MainView mainView;


    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        icon = new Image(App.class.getResourceAsStream("SimIcon.png"), 18, 18, false, false);
        stage.getIcons().add(icon);

        EventHandler<ActionEvent> miniEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                stage.setIconified(true);
            }
        };

        EventHandler<ActionEvent> exitEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                stage.close();
            }
        };
        
        mainView = new MainView(icon, miniEvent, exitEvent);
        
        scene = mainView.getScene();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}