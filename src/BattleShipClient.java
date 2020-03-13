package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BattleShipClient extends Application {
    int[][] board = new int[10][10];
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane pane = new GridPane();
        pane.setMinHeight(600);

        Canvas canvas = new Canvas(1050,500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 50; i <= 500; i+=50) {
            gc.strokeLine(i,0,i,500);
            gc.strokeLine(0,i,500,i);
        }
        for (int i = 550; i <= 1050; i+=50) {
            gc.strokeLine(i,0,i,500);
            gc.strokeLine(550,i-550,1050,i-550);
        }

        TextArea ta = new TextArea();
        TextField tf = new TextField();


        pane.add(canvas,0,0);
        pane.add(tf,0,1);
        pane.add(ta,0,2);
        stage.setScene(new Scene(pane));
        stage.setTitle("BattleShipClient");
        stage.show();

        tf.setOnAction(e -> {
            try {
                String coordinatesToFire = tf.getText();
                toServer.writeUTF(coordinatesToFire);

                toServer.flush();
                String message = fromServer.readUTF();

                ta.appendText(message + "\n");

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        try {
            //Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            //create an input stream to send data to the server
            fromServer = new DataInputStream(socket.getInputStream());

            //create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }

    }
}
