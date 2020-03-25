package another;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class NewBattleShipGame extends Application {

    final int BOARD_SIZE = 10; // the game board will have this number of rows and columns
    final int CELL_NUM = BOARD_SIZE * BOARD_SIZE; // number of cells on a board
    StackPane stackPane;
    //TestBoard playerBoard = new TestBoard("player"); // will contain grid of buttons(cells)
    //TestBoard opponentBoard = new TestBoard("cpu");

    GridCell[] playerCells = new GridCell[CELL_NUM]; // use to access cells on players board
    GridCell[] cpuCells = new GridCell[CELL_NUM];

    String gamePhase; // off, placement, on, end
    boolean gameOn = false; // has a game started? is it active now?
    boolean turn = true; // true = player's turn

    @Override
    public void start(Stage stage) throws Exception {

        // initialize panes

        stackPane = new StackPane();
        stackPane.setPadding(new Insets(10,10,10,10));

        GamePanel gamePanel = new GamePanel();
        playerCells = gamePanel.getCells("player");
        cpuCells = gamePanel.getCells("cpu");
        setListener(); // sets event listener to cells
        stackPane.getChildren().add(gamePanel);

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

    // set mouse click listeners to cpu's GridCells
    // no need to set it on player's cells since cpu won't be clicking cells to make move
    void setListener() {
        for (int i = 0; i < CELL_NUM; i++) {
            cpuCells[i].setOnAction( actionEvent -> {
                clickCell( (GridCell)actionEvent.getSource() );
            } );
        }
    }

    void changeTurn() {
        turn = !turn;
    }

    void startGame() {

    }

    // player clicks a cell
    void clickCell(GridCell cell) {
        System.out.println(cell.getOwner() + "'s cell[" + cell.getIndex() + "](" + cell.getCol() + "," + cell.getRow()
                            + ") clicked!");

        return;
    }

    // cpu's move will be passed using thie method
    boolean clickCell(int cellIndex) {
        return true;
    }

    int makeRandomMove() {
        return (int)( Math.random() * ( 99 - 0 ) + 0 ) ;
    }

}
