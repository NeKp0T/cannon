package com.example.cannon.application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameUI {
    private static final int STEP_TIME = 10; // ms

    private final Runnable toMenu;

    private GameInstance game;
    private Canvas objectsCanvas;
    private ApplicationState state = ApplicationState.WAITING_COMMAND;

    public GameUI(Runnable toMenu) {
        this.toMenu = toMenu;
    }

    public void start(Stage stage) throws Exception {
        System.out.println("GameUI.start");
        game = new GameInstance(2);

        var gameScene = createScene(game);

        stage.setScene(gameScene);

//        stage.show();

        gameScene.setOnKeyPressed(event -> {
            if (state != ApplicationState.WAITING_COMMAND) {
                return;
            }
            switch (event.getCode()) { // TODO move to a configuration file
                case D:
                    game.commandPlayer(GameInstance.Control.RIGHT);
                    break;
                case A:
                    game.commandPlayer(GameInstance.Control.LEFT);
                    break;
                case Q:
                    game.commandPlayer(GameInstance.Control.AIM_LEFT);
                    break;
                case E:
                    game.commandPlayer(GameInstance.Control.AIM_RIGHT);
                    break;
                case SPACE:
                    game.commandPlayer(GameInstance.Control.FIRE);
                    break;
                case DIGIT1:
                    game.commandPlayer(GameInstance.Control.SELECT_0);
                    break;
                case DIGIT2:
                    game.commandPlayer(GameInstance.Control.SELECT_1);
                    break;
                case DIGIT3:
                    game.commandPlayer(GameInstance.Control.SELECT_2);
                    break;
            }

            cycle();
        });
        cycle();
    }

    private void endGame(int winner) {
        if (state == ApplicationState.GAME_ENDED) {
            return;
        }
        state = ApplicationState.GAME_ENDED;

        var alert = new Alert(Alert.AlertType.INFORMATION);

        if (winner >= 0) {
            alert.setTitle("Win");
            alert.setHeaderText("We have a winner");
            alert.setContentText("It's player " + (winner + 1));
        } else {
            alert.setTitle("Draw");
            alert.setHeaderText(null);
            alert.setContentText("They are all dead");
        }

        alert.showAndWait();
        System.out.println("winner alerted");

        toMenu.run();
    }

    private void cycle() {
        if (state == ApplicationState.GAME_ENDED) {
            return;
        }
        state = ApplicationState.SIMULATING;
        var simulatingTimeline = new Timeline();
        var simulateKeyframe = new KeyFrame(
                Duration.millis(STEP_TIME),
                ax -> {
                    if (game.worldPhysicsStep()) {
                        simulatingTimeline.playFromStart();
                    } else {
                        state = ApplicationState.WAITING_COMMAND;

                        if (game.getWinner() != -1) {
                            Platform.runLater(() -> endGame(game.getWinner()));
                        }
                    }
                    renderObjects();
                });
        simulatingTimeline.getKeyFrames().add(simulateKeyframe);
        simulatingTimeline.playFromStart();
    }

    private Scene createScene(GameInstance game) {
        var root = new BorderPane();

        var terrainCanvas = new Canvas();
        terrainCanvas.setHeight(game.getTerrainHeight());
        terrainCanvas.setWidth(game.getTerrainWidth());
        objectsCanvas = new Canvas();
        objectsCanvas.setHeight(game.getTerrainHeight());
        objectsCanvas.setWidth(game.getTerrainWidth());

        game.setCanvas(terrainCanvas);

        renderObjects();
        root.setCenter(terrainCanvas);
        root.getChildren().add(objectsCanvas);

        return new Scene(root, game.getTerrainWidth(), game.getTerrainHeight());
    }

    private void renderObjects() {
        objectsCanvas.getGraphicsContext2D().clearRect(0, 0, objectsCanvas.getWidth(), objectsCanvas.getHeight());
        game.renderObjects(objectsCanvas.getGraphicsContext2D());
    }

    private enum ApplicationState {
        WAITING_COMMAND, SIMULATING, GAME_ENDED
    }
}