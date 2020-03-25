package another;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

// represents each cell on the grid of the game board

public class GridCell extends Button {
    // color enums
    Color CELL_COLOR = Color.LIGHTGREY;
    Color PLAYER_COLOR = Color.CORNFLOWERBLUE;
    Color ENEMY_COLOR = Color.DARKSALMON;
    Color HIT_COLOR = Color.RED;
    Color MISS_COLOR = Color.DARKGRAY;

    // Fields
    //private Button btn; // pointer to the button object on the game board
    private String owner; // -1 = unoccupied, 0 = player, 1 = opponent
    private int index; // this cell's index ( linear )
    private NewShip ship;
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
        owner = "";
        setPrefSize(CELL_SIZE, CELL_SIZE);
        setOnAction(actionEvent -> {

            //this.cellClick();
            // ---test code---
            //System.out.println(owner + "'s cell[" + index + "](" + getCol() + "," + getRow() + ") clicked!");
            //setColor("white");
            //setText("X");
            // ---end of test code---
        });
    }

    // Methods
    public Color getCellColor() {
        return cellColor;
    }


    public String getOwner(){ return this.owner; }
    public int getIndex(){ return this.index; }
    public int getCol(){ return index % BOARD_SIZE; }
    public int getRow(){ return index / BOARD_SIZE; }
    public boolean isHit() { return hit; }
    public boolean hasShip() { return ship != null; }
    public NewShip getShip() { return ship; }


    // depreciated... no longer needed as GridCell itself inherits from Button class
    //public Button getButton() { return this.btn; } // returns pointer to the button element on the game board


    public void setOwner(String owner) { this.owner = owner; } // player or cpu
    public void setShip( NewShip ship ) { this.ship = ship; }

    void updateColor() {

    }

    public void setHit() {
        this.hit = true;
        if( hasShip() ) {
            setColor("red");
        } else {
            setColor( "white" );
        }
    }

    // change the button element's background color
    void setColor(String newColor) { setStyle("-fx-border-color: darkgrey; -fx-background-color: " + newColor); }

    public void cellClick() {



    }
}
