package another;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
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
 * current bug... when placing ships, vertical placement overlaps with horizontal and vice versa
 */

public class NewBattleShipGame extends Application {
    Player player = new Player("player");
    Player cpu = new Player("cpu");

    final int BOARD_SIZE = 10; // the game board will have this number of rows and columns
    final int CELL_NUM = BOARD_SIZE * BOARD_SIZE; // number of cells on a board
    StackPane stackPane;
    //TestBoard playerBoard = new TestBoard("player"); // will contain grid of buttons(cells)
    //TestBoard opponentBoard = new TestBoard("cpu");

    GridCell[] playerCells = new GridCell[CELL_NUM]; // use to access cells on players board
    GridCell[] cpuCells = new GridCell[CELL_NUM];

    int gamePhase; // -1: not started, 0: ship placement, 1: battle, 2: game over
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

        // listeners used during ship placement phase
        for (int i = 0; i < playerCells.length; i++) {
            playerCells[i].setOnMousePressed( mouseEvent -> {
                int shipOrientation;
                if (gamePhase != 0) { return; } // only should occur during ship placement phase
                if(mouseEvent.isPrimaryButtonDown()){
                    shipOrientation = 0;
                    startShipPlacement((GridCell)mouseEvent.getSource(), shipOrientation);
                    System.out.println("ship placement: left click");
                } else if ( mouseEvent.isSecondaryButtonDown()) {
                    shipOrientation = 1;
                    startShipPlacement((GridCell)mouseEvent.getSource(), shipOrientation);
                    System.out.println("ship placement: right click");
                } else {
                    //System.out.println("ship placement: else...");
                    return;
                }

            } );
        }

    }

    void changeTurn() {
        turn = !turn;
    }

    void startGame() {
        gamePhase = 0; // -1: not started, 0: ship placement, 1: battle, 2: game over
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Start placing ships on your board");
        alert.show();

        //startShipPlacement();
    }

    void startBattlePhase() {
        // human player starts first
        turn = true;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Start firing at enemy location !");
        alert.show();


    }

    // player clicks a cell
    void clickCell(GridCell targetCell) {

        if(gamePhase == 1 && !targetCell.isHit()) {
            if ( targetCell.hasShip() ){
                targetCell.setText("X");
                System.out.println("HIT!!!");
            }
            targetCell.setHit();
            checkGameOver();
            changeTurn();
            makeCpuMove();
        }

        // for test runs
        System.out.println(targetCell.getOwner() + "'s targetCell[" + targetCell.getIndex() + "](" + targetCell.getCol() + "," + targetCell.getRow()
                            + ") clicked!");

        return;
    }



    private void makeCpuMove() {
        GridCell target = playerCells[makeRandomMove()];
        while ( target.isHit() ) {
            target = playerCells[makeRandomMove()];
        }
        target.setHit();
        checkGameOver();
        changeTurn();
    }

    private void checkGameOver() {
        // TODO
    }

    // cpu's move will be passed using this method
    boolean clickCell(int cellIndex) {
        return true;
    }

    int makeRandomMove() {
        return (int)( Math.random() * ( 99 - 0 ) + 0 ) ;
    }

    void startShipPlacement(GridCell cell, int shipOrientation) {
        // place player's ships on his/her game board
        // add mouse click listeners to players cells

        for (int i = 0; i < player.getShips().length; i++) {
            NewShip ship = player.getShips()[i];
            if(!ship.isPlaced()){
                if(ship.isValidPlacement(cell,playerCells, shipOrientation)){
                    ship.placeShip(cell, playerCells, shipOrientation);
                    break;
                } else {
                    break;
                }
            }
        }

        // check if all ships are deployed
        boolean allShipsPlaced = true;
        for (int i = 0; i < player.getShips().length; i++) {
            allShipsPlaced = player.getShips()[i].isPlaced();
        }
        if( allShipsPlaced ) {
            System.out.println("All ships deployed!");
            gamePhase = 1;
            placeCpuShips();
            startBattlePhase();
        }

    }

    void placeCpuShips() {
        NewShip[] cpuShips = new NewShip[5];
        cpuShips = cpu.getShips();
        cpuShips[0].placeShip(cpuCells[0],cpuCells,1);
        cpuShips[1].placeShip(cpuCells[4],cpuCells,1);
        cpuShips[2].placeShip(cpuCells[70],cpuCells,0);
        cpuShips[3].placeShip(cpuCells[90],cpuCells,0);
        cpuShips[4].placeShip(cpuCells[45],cpuCells,1);
        System.out.println("CPU ships deployed!");
    }


}
