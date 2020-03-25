package another;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

// this will serve as the main window of the entire game
public class GamePanel extends GridPane {

    // Fields
    BorderPane playerPane = new BorderPane();
    BorderPane cpuPane = new BorderPane();

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

        // place elements to self
        add(timerLabel,0,0);

        // player side elements
        playerPane.setCenter(new TestBoard("player"));
        add(playerBoardLabel,0,1);
        add(playerPane, 0,2);

        // cpu side elements
        cpuPane.setCenter(new TestBoard("cpu"));
        add(cpuBoardLabel,1,1);
        add(cpuPane,1,2);

        // add system log
        add(gameLogTextField,0,3);
    }


    // Methods


}

