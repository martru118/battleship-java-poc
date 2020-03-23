package test;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

// represents each cell on the grid of the game board
// has pointer to button elements which are placed on the gridpane for access and manipulation
// contains each cell's data

public class GridCell {
    // color enums
    Color CELL_COLOR = Color.LIGHTGREY;
    Color PLAYER_COLOR = Color.CORNFLOWERBLUE;
    Color ENEMY_COLOR = Color.DARKSALMON;
    Color HIT_COLOR = Color.RED;
    Color MISS_COLOR = Color.DARKGRAY;

    // Fields
    private Button btn; // pointer to the button object on the game board
    private int owner; // -1 = unoccupied, 0 = player, 1 = opponent
    private final int index; // this cell's index ( linear )
    boolean hit = false; // has an attack landed on this cell?
    Color cellColor = CELL_COLOR;
    String cellColorString;



    // Constructor
    public GridCell(int owner, Button btn) {
        this.owner = owner;
        this.btn = btn;
        this.index = Integer.parseInt(btn.getId()) ;
        this.cellColor = getCellColor( owner );
    }

    // Methods
    Color getCellColor(int owner) {
        if ( owner == 0 ) {
            return PLAYER_COLOR;
        } else if ( owner == 1 ) {
            return ENEMY_COLOR;
        } else {
            return CELL_COLOR;
        }
    }


    public int getOwner(){ return this.owner; }
    public int getIndex(){ return this.index; }
    public boolean isHit() { return hit; }
    public Button getButton() { return this.btn; } // returns pointer to the button element on the game board


    void setOwner(int owner) {
        // -1 = unoccupied, 0 = player, 1 = opponent
        this.owner = owner;
    }

    void updateColor() {

    }


    void setColor(String newColor) {
        btn.setStyle("-fx-border-color: darkgrey; -fx-background-color: " + newColor);
    }
}
