package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class BattleShipServer extends Application {
    private boolean[][] board = new boolean[10][10];
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private TextArea ta = new TextArea();

    @Override
    public void start(Stage stage) throws Exception {
        GridPane pane = new GridPane();
        pane.setMinHeight(600);


        pane.add(ta,0,2,2,1);
        pane.setHgap(50);
        stage.setScene(new Scene(pane));
        stage.setTitle("BattleShip");
        stage.show();


        Thread clientConnect = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> ta.appendText("Server started at " +  new Date() + '\n'));
                Socket socket = serverSocket.accept();

                DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());

                while(true) {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        clientConnect.start();
    }

}
