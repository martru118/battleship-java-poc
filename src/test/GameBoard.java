package test;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import sample.Ship;

public class GameBoard {

    // Fields
    final int boardSize; // the game board will have this many number of rows and columns
    GridCell[] gridCells;

    // Constructor
    public GameBoard(int boardSize, Button[] buttons) {
        this.boardSize = boardSize;
        this.gridCells = new GridCell[boardSize * boardSize];
        initializeCells(buttons);
    }


    // Methods

    void initializeCells(Button[] cells) {
        for (int i = 0; i < boardSize * boardSize; i++) {
            gridCells[i] = new GridCell( -1, cells[i]);
        }
    }

    // this cell has been fired at
    // update accordingly
    void updateCell(Button cellBtn) {
        GridCell cell = gridCells[Integer.parseInt(cellBtn.getId())]; // fetch GridCell instance connected to this button

        if ( cell.isHit() ) { return; } // return if this cell's already been hit

        // TODO dertermine if the cell is occupied and update cell accordingly
        if ( cell.getOwner() == -1 ) {
            cell.getButton().setText("X");
        }
    }
}

