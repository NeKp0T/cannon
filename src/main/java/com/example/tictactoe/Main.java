package com.example.tictactoe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static com.example.tictactoe.GameInstance.FIELD_SIZE;

public class Main extends Application {

    private final static int CELL_LENGTH = 200;
    private final GameInstance gameInstance = new GameInstance();
    private final Label statusLine = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private Pane createContent() {

        var root = new BorderPane();

        var table = new GridPane();
        table.setPrefSize(CELL_LENGTH * FIELD_SIZE, CELL_LENGTH * FIELD_SIZE);
        table.setAlignment(Pos.CENTER);
        table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        table.setPadding(new Insets(15));

        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {

                var cell = new Cell(i, j);

                cell.setMaxWidth(Double.MAX_VALUE);
                cell.setMaxHeight(Double.MAX_VALUE);
                table.add(cell, i, j);
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);

            }
        }

        root.setCenter(table);

        var bottom = new HBox(10);
        statusLine.setText(gameInstance.getStatus());
        statusLine.setFont(new Font(30));
        bottom.getChildren().add(statusLine);

        root.setBottom(bottom);

        return root;
    }

    private class Cell extends GridPane {

        private final Button button = new Button();
        private final int coordinateX;
        private final int coordinateY;

        private Cell(int coordinateX, int coordinateY) {
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;

            setPrefSize(CELL_LENGTH, CELL_LENGTH);

            button.setOnAction(event -> {
                try {
                    gameInstance.put(coordinateX, coordinateY);
                } catch (ImpossibleMoveException e) {
                    // Wrong move
                    // Let's do nothing
                }

                draw();
                statusLine.setText(gameInstance.getStatus());

            });
            getChildren().add(button);

        }

        public void draw() {
            switch(gameInstance.get(coordinateX, coordinateY)) {
                case CROSS:
                    drawCross();
                    break;
                case NOUGHT:
                    drawCircle();
                    break;
            }
        }

        private void drawCross(){
            double width = getWidth(), height = getHeight();
            double scale = 0.2f;
            Line line1 = new Line(scale * width, scale * height, (1 - scale) * width, (1 - scale) * height);
            Line line2 = new Line(scale * width, (1 - scale) * height, (1 - scale) * width, scale * height);

            getChildren().addAll(line1, line2);
        }

        private void drawCircle() {
            double w = getWidth(), h = getHeight();
            double radius = 0.4f;
            Ellipse ellipse = new Ellipse(w / 2, h / 2, radius * w, radius * h);
            ellipse.setStroke(Color.BLACK);
            ellipse.setFill(null);
            getChildren().add(ellipse);
        }


    }


}