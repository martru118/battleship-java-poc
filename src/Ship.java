public class Ship {
    int spaces;
    String name;
    boolean sunk;
    int[][] coordinates;
    int hits;
    Ship(int spaces, String name){
        this.spaces = spaces;
        this.name = name;

        hits = 0;
        this.sunk = false;
        coordinates = new int[spaces][2];
    }
    public boolean isHit(int[] coordinates, boolean[][] board) {
        if (board[coordinates[0]][coordinates[1]]) {
            return true;
        }
        return false;
    }
    public int[][] getCoordinates() {return coordinates;}
    public void setCoordinates(int[][] c) {
        coordinates = c;
    }
    public boolean isSunk() {
        if (hits == spaces) {
            return true;
        }
        return false;
    }

}
