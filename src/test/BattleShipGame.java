package test;

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
    GridPane playerBoard; // will contain grid of buttons(cells)
    GridPane opponentBoard;
    Button[] playerGridButtons = new Button[BOARD_SIZE * BOARD_SIZE];
    Button[] opponentGridButtons = new Button[BOARD_SIZE * BOARD_SIZE];
    GameBoard playerGameBoard;
    GameBoard opponentGameBoard;
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

        playerBoard = new GridPane();
        opponentBoard = new GridPane();

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



        // initialize elements
        createButtons( playerGridButtons, true );
        createButtons( opponentGridButtons, false );

        playerGameBoard = new GameBoard(BOARD_SIZE, playerGridButtons);
        opponentGameBoard = new GameBoard(BOARD_SIZE, opponentGridButtons);
        // style grid cells
        playerGameBoard.initializeCells( playerGridButtons );
        opponentGameBoard.initializeCells( opponentGridButtons );

        // place buttons on the game board
        placeButtons( playerGridButtons, playerBoard );
        placeButtons( opponentGridButtons, opponentBoard );


        // set stage
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();


        startGame();

    }

    void createButtons( Button[] cellButtons, boolean player ) {
        final int BUTTON_SIZE = 40;
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            cellButtons[i] = new Button();
            cellButtons[i].setId(String.valueOf(i)); // this will be used as this cell's index
            cellButtons[i].setPrefSize(BUTTON_SIZE,BUTTON_SIZE);
            cellButtons[i].setMinSize(BUTTON_SIZE,BUTTON_SIZE);
            cellButtons[i].setPadding(new Insets(0.5,0.5,0.5,0.5));
            cellButtons[i].setOnAction(actionEvent -> {
                if( !player ) { // player can't attack his own board
                    makeMove( (Button)actionEvent.getSource() );
                    //updateCell( (Button)actionEvent.getSource() );
                }
            });
        }
    }

    private void makeMove(Button source) {

        if( opponentGameBoard.updateCell( source ) ){ // ? valid move
            changeTurn();
            playerGameBoard.updateCell( playerGridButtons[makeRandomMove()] );
            changeTurn();
        }

    }

    // adds buttons on the game board pane to form the grid
    void placeButtons( Button[] cellButtons, GridPane gameBoard) {
        int x;
        int y;
        for (int i = 0; i < 100; i++) {
            x = i % BOARD_SIZE;
            y = i / BOARD_SIZE;
            gameBoard.add(cellButtons[i], x, y);
        }
    }

    // shot fired at a cell, update the cell
    void updateCell(Button cell) {
        if( turn ) {
            opponentGameBoard.updateCell( cell );
            playerGameBoard.updateCell( playerGridButtons[makeRandomMove()] );
        } else {
            // TODO make AI make a move
            // for now make a random move on opponent's turn
            playerGameBoard.updateCell( playerGridButtons[makeRandomMove()] );
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
