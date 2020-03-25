package another;

/*

Test board using a version of GridCell class which inherits from Button class.

 */

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class TestBoard extends GridPane {
    final int BOARD_SIZE = 10;
    private BorderPane borderPane = new BorderPane();
    private GridPane gridPane = new GridPane(); // this is the board where all cells go on to form a grid
    private Scene scene = new Scene(borderPane);
    private GridCell[] gridCells = new GridCell[BOARD_SIZE * BOARD_SIZE];
    //boolean hasBoard = false;
    final String owner;

    // Constructor
    public TestBoard(String owner) {
        super();
        this.owner = owner;
        initializeBoard();
    }

    void initializeBoard() {
        // add gridCells to gridPane
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            int row = i / BOARD_SIZE;
            int col = i % BOARD_SIZE;
            gridCells[i] = new GridCell(i);
            this.add(gridCells[i], col, row);
        }
        //hasBoard = true;
    }

    public GridPane getGameBoard(){
        //if( !hasBoard ){ initializeBoard(); }
        return this.gridPane;
    }

    public GridCell getGridCell(int index) {
        return gridCells[index];
    }

}
