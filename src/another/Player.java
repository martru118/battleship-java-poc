package another;


import javafx.application.Platform;

public class Player {
    // Fields
    private TestBoard gameBoard; // this player's game board

    private NewShip destroyer = new NewShip(4, "Destroyer");
    private NewShip battleship = new NewShip(3,"Battleship");
    private NewShip patrol = new NewShip(2,"Patrol");
    private NewShip submarine = new NewShip(3,"Submarine");
    private NewShip carrier = new NewShip(5,"Carrier");
    private final NewShip[] ships = {destroyer, battleship, patrol, submarine, carrier};
    private String name; // player's name
    private Double score;

    // Constructor
    public Player(String name) {
        this.name = name;
        score = 0.0;
    }

    // Methods

    public NewShip getShipByName(String shipName) throws Exception{
        for (int i = 0; i < ships.length; i++) {
            if ( ships[i].getName() == shipName) {
                return ships[i];
            }
        }
        throw new Exception("No such ship found");
    }

    public NewShip[] getShips() {
        return ships;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }

    public void setGameBoard(TestBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public TestBoard getGameBoard() {
        return gameBoard;
    }
}
