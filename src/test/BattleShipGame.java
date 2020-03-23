package test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// TODO set up game GUI : player and opponent boards, log box,
//  implement hit/miss mechanism

public class BattleShipGame extends Application {

    final int boardSize = 10; // the game board will have this number of rows and columns
    StackPane stackPane;
    GridPane playerBoard;
    GridPane opponentBoard;
    Button[] playerGridButtons = new Button[boardSize * boardSize];
    GameBoard gameBoard;
    boolean turn = true; // true = player's turn

    @Override
    public void start(Stage stage) throws Exception {

        // initialize panes
        stackPane = new StackPane();
        stackPane.setPadding(new Insets(10,10,10,10));
        playerBoard = new GridPane();
        stackPane.getChildren().add(playerBoard);



        // create elements
        createButtons();

        gameBoard = new GameBoard(boardSize, playerGridButtons);
        // style grid cells
        gameBoard.initializeCells(playerGridButtons);

        // place buttons on the game board
        placeButtons();


        // set stage
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();
    }

    void createButtons() {
        int buttonSize = 30;
        for (int i = 0; i < boardSize * boardSize; i++) {
            playerGridButtons[i] = new Button();
            playerGridButtons[i].setId(String.valueOf(i)); // this will be used as this cell's index
            playerGridButtons[i].setPrefSize(buttonSize,buttonSize);
            playerGridButtons[i].setMinSize(buttonSize,buttonSize);
            playerGridButtons[i].setPadding(new Insets(0.5,0.5,0.5,0.5));
            playerGridButtons[i].setOnAction(actionEvent -> {
                if( turn ) { // only if it's player's turn
                    updateCell( (Button)actionEvent.getSource() );
                }

            });
        }
    }

    void placeButtons() {
        int x;
        int y;
        for (int i = 0; i < 100; i++) {
            x = i % boardSize;
            y = i / boardSize;
            playerBoard.add(playerGridButtons[i], x, y);
        }
    }

    // shot fired at a cell, update the cell
    void updateCell(Button cell) {
        gameBoard.updateCell( cell );
    }

    void changeTurn() {
        turn = !turn;
    }

}
