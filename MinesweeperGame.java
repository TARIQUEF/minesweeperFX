package game;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MinesweeperGame extends Application {

    private static final int SIZE = 10; // fixed size of 10x10
    private GameLogic gameLogic;
    private Button[][] buttons = new Button[SIZE][SIZE];
    private Label statusLabel; // status wordss

    private Label flagLabel; //flags



    @Override
    public void start(Stage primaryStage) {

        int flagNum = 10;
        gameLogic = new GameLogic(10, flagNum); // initialize game

        GridPane grid = new GridPane(); //root layout for grid
        grid.setAlignment(Pos.CENTER);

        statusLabel = new Label("Welcome to Minesweeper!");
        statusLabel.setStyle("-fx-font-size: 16px;");

        flagLabel = new Label("Flags:" + flagNum);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(statusLabel,flagLabel, grid);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                final int x = i;
                final int y = j;
                Button button = new Button("?");
                button.setPrefSize(40, 40);

                button.setOnAction(e -> handleButtonClick(x, y, button));
                button.setOnContextMenuRequested(e -> handleRightClick(x, y, button));

                buttons[i][j] = button;
                grid.add(button, j, i);
            }
        }

        Scene scene = new Scene(vbox, 450, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Minesweeper");
        primaryStage.show();
    }

    private void handleButtonClick(int x, int y, Button button) {
        if (gameLogic.checkMines(x, y)) { // If mine
            button.setText("M"); // sshow mine if clicked
            button.setStyle("-fx-background-color: hotpink;"); // pink style for mines
            statusLabel.setText("Game Over! You hit a mine.");
            showGameOverDialog(); // show game over
        } else { // itherwise uncover regularly
            gameLogic.uncover(x, y);
            updateButtonLabels(); // update all button labels
        }
    }

    private void handleRightClick(int x, int y, Button button) {
        gameLogic.addFlag(x, y); //inside
        updateButtonLabels(); // ppdate all button labels after
        flagLabel.setText("Flags:" + gameLogic.getFlagNum());
    }

    private void updateButtonLabels() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Button button = buttons[i][j];
                String value = gameLogic.getBoardVisibleValue(i, j); // get the visible value for the cell
                button.setText(value); // update the button text

                // Set button styles based on its value
                if (value.equals("M")) {
                    button.setStyle("-fx-background-color: hotpink; -fx-font-weight: bold;"); // red mines
                } else if (value.equals("_")) {
                    button.setStyle("-fx-background-color: pink;"); // empty then grey
                } else if (value.equals("F")) {
                    button.setStyle("-fx-background-color: yellow;"); // yellow flagg
                } else {
                    button.setStyle("-fx-background-color: beige;"); // reg cells
                }


                button.setPrefSize(40, 40); // maintain button size
            }
        }
    }


    private void showGameOverDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("You hit a mine!");
        alert.setContentText("Do you want to play again?");

        ButtonType playAgainButton = new ButtonType("Play Again");
        ButtonType exitButton = new ButtonType("Exit");

        alert.getButtonTypes().setAll(playAgainButton, exitButton);

        alert.showAndWait().ifPresent(response -> { // from stackoverflow
            if (response == playAgainButton) {
                restartGame();
            } else if (response == exitButton) {
                System.exit(0);
            }
        });
    }

    private void restartGame() {
        // reinitialize the game logic
        int flagNum = 10;
        gameLogic = new GameLogic(10, flagNum);

        // re put the buttons
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Button button = buttons[i][j];
                button.setText("?");
                button.setStyle("-fx-background-color: beige;"); // reset button style
            }
        }

        // uopdate status label
        statusLabel.setText("New game!");
        flagLabel.setText("Flags:" + flagNum);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
