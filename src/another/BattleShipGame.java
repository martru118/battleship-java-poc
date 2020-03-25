package another;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO
//  set up game GUI : player and opponent boards, log/chat box
//  ship placement
//  opponent AI
//  implement hit/miss mechanism
//  multi-threading
//  game end

/**
 * Flow of game
 */

public class BattleShipGame extends Application {

    final int BOARD_SIZE = 10; // the game board will have this number of rows and columns
    StackPane stackPane;
    TestBoard playerBoard = new TestBoard("player"); // will contain grid of buttons(cells)
    TestBoard opponentBoard = new TestBoard("cpu");

    boolean turn = true; // true = player's turn

    @Override
    public void start(Stage stage) throws Exception {

        // initialize panes
        /*

               ------------StackPane-------------
                -----------mainVBox----------------
                   --------HBox------------
                   --------HBox------------
                   --------HBox------------
                -------------------------------
              -----------------------------------
         */

        stackPane = new StackPane();
        stackPane.setPadding(new Insets(10,10,10,10));

        VBox mainVBox = new VBox();

        HBox labelHBox = new HBox();
        labelHBox.setSpacing( 400.0 ); // place labels above corresponding game boards
        Label playerBoardLabel = new Label("PLAYER");
        Label opponentBoardLabel = new Label("OPPONENT");
        labelHBox.getChildren().addAll(playerBoardLabel, opponentBoardLabel);

        HBox boardHBox = new HBox();
        boardHBox.setSpacing( 30.0d );
        boardHBox.getChildren().addAll( playerBoard, opponentBoard );

        mainVBox.getChildren().addAll(labelHBox, boardHBox);
        stackPane.getChildren().add(mainVBox);




        // set stage
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();


        startGame();

    }


    private void makeMove(Button source) {

    }

    // shot fired at a cell, update the cell
    void updateCell(Button cell) {
        if( turn ) {
            //opponentGameBoard.updateCell( cell );
            //playerGameBoard.updateCell( playerGridButtons[makeRandomMove()] );
        } else {
            // TODO make AI make a move
            // for now make a random move on opponent's turn
            //playerGameBoard.updateCell( playerGridButtons[makeRandomMove()] );
        }

        changeTurn();

    }

    void changeTurn() {
        turn = !turn;
    }

    void startGame() {

    }

    int makeRandomMove() {
        return (int)( Math.random() * ( 99 - 0 ) + 0 ) ;
    }

}
