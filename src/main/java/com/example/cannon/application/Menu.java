package com.example.cannon.application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Menu {
    private static final double MENU_HEIGHT = 480;
    private static final double MENU_WIDTH = 640;

    public static void showMenu(Stage stage) {
        Scene menuScene = getMenuScene(stage);
        stage.setResizable(false);
        stage.setScene(menuScene);
    }

    private static Scene getMenuScene(Stage stage) {
        var menuPane = new StackPane();
        Button b = new Button("Start");
        b.setOnAction(e -> startGame(stage));
        menuPane.getChildren().add(b);

        return new Scene(menuPane, MENU_WIDTH, MENU_HEIGHT);
    }

    private static void startGame(Stage stage) {
        GameUI gameUI = new GameUI(() -> {
            System.out.println("Switching back to menu");
            showMenu(stage);
        });
        try {
            gameUI.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
