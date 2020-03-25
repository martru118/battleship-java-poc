package another;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import sample.Ship;

// represents each cell on the grid of the game board
// has pointer to button elements which are placed on the gridpane for access and manipulation
// contains each cell's data

public class GridCell extends Button {
    // color enums
    Color CELL_COLOR = Color.LIGHTGREY;
    Color PLAYER_COLOR = Color.CORNFLOWERBLUE;
    Color ENEMY_COLOR = Color.DARKSALMON;
    Color HIT_COLOR = Color.RED;
    Color MISS_COLOR = Color.DARKGRAY;

    // Fields
    //private Button btn; // pointer to the button object on the game board
    private int owner; // -1 = unoccupied, 0 = player, 1 = opponent
    private int index; // this cell's index ( linear )
    private Ship ship;
    boolean hit; // has an attack landed on this cell?
    Color cellColor = CELL_COLOR;
    String cellColorString;
    final double CELL_SIZE = 40.0;
    final int BOARD_SIZE = 10;

    // Constructor
    public GridCell(int index) {
        super();
        this.index = index;
        ship = null;
        hit = false;
        owner = -1;
        setPrefSize(CELL_SIZE, CELL_SIZE);
        setOnAction(actionEvent -> {
            System.out.println("cell[" + index + "](" + getCol() + "," + getRow() + ") clicked!");

            // ---test code---
            setColor("white");
            setText("X");
            // ---end of test code---
        });
    }

    // Methods
    public Color getCellColor() {
        return cellColor;
    }


    public int getOwner(){ return this.owner; }
    public int getIndex(){ return this.index; }
    public int getCol(){ return index % BOARD_SIZE; }
    public int getRow(){ return index / BOARD_SIZE; }
    public boolean isHit() { return hit; }
    public Ship getShip() { return ship; }


    // depreciated... no longer needed as GridCell itself inherits from Button class
    //public Button getButton() { return this.btn; } // returns pointer to the button element on the game board


    public void setOwner(int owner) { this.owner = owner; } // -1 = unoccupied, 0 = player, 1 = opponent
    public void setShip( Ship ship ) { this.ship = ship; }

    void updateColor() {

    }

    public void setHit() {
        this.hit = true;
    }

    // change the button element's background color
    void setColor(String newColor) { setStyle("-fx-border-color: darkgrey; -fx-background-color: " + newColor); }
}
