package another;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        TestBoard playerBoard = new TestBoard("player");
        //GridPane  = boardBuilder.getGameBoard();
        Scene scene = new Scene(playerBoard);

        stage.setScene(scene);
        stage.show();
    }
}
