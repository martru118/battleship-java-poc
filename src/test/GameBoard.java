package test;

import javafx.scene.control.Button;

public class GameBoard {

    // Fields
    final int BOARD_SIZE; // the game board will have this many number of rows and columns
    GridCell[] gridCells;

    // Constructor
    public GameBoard(int boardSize, Button[] buttons) {
        this.BOARD_SIZE = boardSize;
        this.gridCells = new GridCell[boardSize * boardSize];
        initializeCells(buttons);
    }


    // Methods

    void initializeCells(Button[] cells) {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            gridCells[i] = new GridCell( -1, cells[i]);
        }
    }

    // this cell has been fired at
    // update accordingly
    // returns true if it's a valid move, false otherwise
    boolean updateCell(Button cellBtn) {
        GridCell cell = gridCells[Integer.parseInt(cellBtn.getId())]; // fetch GridCell instance connected to this button

        if ( cell.isHit() ) { return false; } // return if this cell's already been hit

        // TODO determine if the cell is occupied and update cell status
        //  should change color differently depending on ownership of the cell
        cell.getButton().setText("X");
        if ( cell.getOwner() == -1 ) { // empty cell
            cell.setColor("aqua");
        } else if ( cell.getOwner() == 0) {
            cell.setColor("salmon");
        } else {
            cell.setColor("coral");
        }
        cell.setHit();

        return true;
    }
}

