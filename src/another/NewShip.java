package another;


/**
 *
 */
public class NewShip {
    private int spaces;
    private String name;
    private boolean sunk;
    private int coordinate; // left upper most cell of this ship.. use index ( 0.. 99 )
    private GridCell[] cells; // cells that this ship is occupying
    private int orientation;
    private int hits; // number of hits taken, if equal or greater than ship's spaces, the ship is sunk
    private boolean isPlaced; // has the ship been placed on the board?
    String shipCellColor = "silver";

    /**
    NewShip(String name, int spaces, int coordinate){
        this.spaces = spaces;
        this.name = name;
        hits = 0;
        this.sunk = false;
        this.coordinate = coordinate;
    }
    */

    /**
     * this ship will have to be placed on a game board
     * @param name name of this ship
     * @param spaces number of cells this ship will occupy
     */
    NewShip(int spaces, String name){
        this.name = name;
        this.spaces = spaces;
        this.orientation = -1;
        hits = 0;
        this.sunk = false;
        this.coordinate = -1;
        this.cells = new GridCell[spaces];
        isPlaced = false;
    }
    /**
     *
     * @param name name of this ship
     * @param spaces number of cells this ship will occupy
     * @param orientation HORIZONTAL(0) or VERTICAL(1)
     * @param cell the left upper most cell this ship will occupy
     */
    NewShip(int spaces, String name, int orientation, GridCell cell){
        this.name = name;
        this.spaces = spaces;
        this.orientation = orientation;
        hits = 0;
        this.sunk = false;
        this.coordinate = cell.getIndex();
        this.cells = new GridCell[spaces];
        isPlaced = false;
        // first check if it's valid position for placement
        /**
        if( isValidPlacement(cell) ){
            isPlaced = true;
        }
         */
    }

    //getters and setters
    public int getSpaces() {return spaces;}
    public String getName() {return name;}
    public int getCoordinate() {return coordinate;}
    public void setCoordinates(int coordinate) { this.coordinate = coordinate; }

    //check if ship has been hit
    /**
    public boolean isHit(int coordinate) {
        for (int i = 0; i < this.spaces; i++) {
            if ( orientation == HORIZONTAL) {

            } else { // vertical

            }
        }

    }
    */
    /**
     *
     * @param target the target cell of attack
     * @return returns true if it's a hit
     */
    public boolean isHit(GridCell target) {
        for (int i = 0; i < cells.length; i++) {
            if(cells[i].equals(target)){
                return true;
            }
        }
        return false;
    }

    //check if ship has been sunk
    public boolean isSunk() {
        return hits == spaces;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    // is there a cell to the right to this cell/position?
    boolean hasSpaceToRight(int coordinate) {
      if ( (coordinate % 10) < 9) {
          return true;
      }
      return false;
    };
    boolean hasSpaceToRight(GridCell cell) {
        if ( cell.getCol() < 9) {
            return true;
        }
        return false;
    };

    // is there a cell below to this cell/position?
    boolean hasSpaceBelow( int coordinate ) {
        if ( (coordinate / 10) < 9) {
            return true;
        }
        return false;
    }
    boolean hasSpaceBelow( GridCell cell ) {
        if ( cell.getRow() < 9) {
            return true;
        }
        return false;
    }

    /**
     * check if the ship can be placed at this cell
     * @param cell the left upper most cell this ship will occupy
     * @param  newOrientation horizontal(0) or vertical(1)
     * @return true if the ship can be placed here
     */
    boolean isValidPlacement(GridCell cell, GridCell[] playerCells, int newOrientation) {
        if( newOrientation == HORIZONTAL ) {
            // check to right
            if ( cell.getCol() + spaces <= 10 ) {
                // check collision with other ships
                boolean clear = true;
                for (int i = 0; i < spaces; i++) {
                    clear = playerCells[cell.getIndex() + i].getShip() == null;
                }
                if ( clear ){ return true; }
            }
        } else { // vertical
            // check below
            if ( cell.getRow() + spaces <= 10 ) {
                // check collision with other ships
                boolean clear = true;
                for (int i = 0; i < spaces; i++) {
                    clear = playerCells[cell.getIndex() + i*10].getShip() == null;
                }
                if (clear) { return true; }

            }
        }
        return false;
    }

    /**
     *
     * @param cell left upper most cell this ship will occupy
     */
    void placeShip(GridCell cell, GridCell[] playerCells, int shipOrientation){
        orientation = shipOrientation;
        if( orientation == HORIZONTAL) {
            for (int i = 0; i < spaces; i++) {
                cells[i] = playerCells[cell.getIndex() + i];
                playerCells[cell.getIndex() + i].setShip(this);
                playerCells[cell.getIndex() + i].setStyle("-fx-border-color: darkgrey; -fx-background-color: "
                                                            + shipCellColor);
            }
        } else {
            for (int i = 0; i < spaces; i++) { // vertical
                cells[i] = playerCells[cell.getIndex() + i*10];
                playerCells[cell.getIndex() + i*10].setShip(this);
                playerCells[cell.getIndex() + i*10].setStyle("-fx-border-color: darkgrey; -fx-background-color: "
                        + shipCellColor);;
            }

        }
        isPlaced = true;
    }

    // ENUMs for ship orientation
    static private final int HORIZONTAL = 0;
    static private final int VERTICAL = 1;
}