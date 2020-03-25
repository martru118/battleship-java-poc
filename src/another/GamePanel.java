package another;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// this will serve as the major panel holding game boards
public class GamePanel extends GridPane {

    // Fields
    BorderPane playerPane = new BorderPane();
    BorderPane cpuPane = new BorderPane();

    // Menubar elements
    MenuBar menubar = new MenuBar();
    Menu menu = new Menu();

    // rows and columns markers
    final double BUTTON_SIZE = 40.0;
    HBox hBox1 = new HBox();
    Button[] hBtns1 = new Button[11]; // column markers need an extra placeholder to line up properly
    VBox vBox1 = new VBox();
    Button[] vBtns1 = new Button[10];

    HBox hBox2 = new HBox();
    Button[] hBtns2 = new Button[10];
    VBox vBox2 = new VBox();
    Button[] vBtns2 = new Button[10];


    TestBoard playerBoard;
    TestBoard cpuBoard;

    Label playerBoardLabel = new Label("PLAYER");
    Label cpuBoardLabel = new Label("CPU");

    TextField gameLogTextField = new TextField("game log");

    Label timerLabel = new Label("TIMER"); // placeholder for timer



    // Constructor
    public GamePanel() {
        super();
        initialize();
    }

    private void initialize() {
        setHgap(5.0);

        // add  elements
        // for now the first row of this main grid pain is empty

        // place elements to self
        add(timerLabel,0,1);

        // rows and columns markers
        buildMarkers();

        // player side elements
        playerBoard = new TestBoard("player");
        playerPane.setCenter( playerBoard );
        playerPane.setTop(hBox1);
        playerPane.setLeft(vBox1);
        add(playerBoardLabel,0,2);
        add(playerPane, 0,3);

        // cpu side elements
        cpuBoard = new TestBoard("cpu");
        cpuPane.setCenter( cpuBoard );
        cpuPane.setTop(hBox2);
        cpuPane.setRight(vBox2);
        add(cpuBoardLabel,1,2);
        add(cpuPane,1,3);

        // add additional buttons here


        // add system log
        add(gameLogTextField,0,4);
    }

    void buildMarkers() {
        hBtns1[0] = new Button();
        hBtns1[0].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        hBtns1[0].setVisible(false);
        hBox1.getChildren().add(hBtns1[0]);
        for (int i = 0; i < 10; i++) {
            hBtns1[i+1] = new Button(String.valueOf(i));
            hBtns1[i+1].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            hBox1.getChildren().add(hBtns1[i+1]);
            vBtns1[i] = new Button(String.valueOf(i));
            vBtns1[i].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            vBox1.getChildren().add(vBtns1[i]);
        }



        for (int i = 0; i < 10; i++) {
            hBtns2[i] = new Button(String.valueOf(i));
            hBtns2[i].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            hBox2.getChildren().add(hBtns2[i]);
            vBtns2[i] = new Button(String.valueOf(i));
            vBtns2[i].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            vBox2.getChildren().add(vBtns2[i]);
        }
    }


    // Methods
    public GridCell[] getCells(String owner) {
        if ( owner == "player") {
            return playerBoard.getGridCells();
        } else if ( owner == "cpu") {
            return cpuBoard.getGridCells();
        }
        return null;
    }


}

