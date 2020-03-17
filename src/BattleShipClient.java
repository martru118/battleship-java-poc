package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

        Canvas playerCanvas = new Canvas(500,500);
        Canvas opponentCanvas = new Canvas(500,500);
        GraphicsContext gcP = playerCanvas.getGraphicsContext2D();
        GraphicsContext gcO = opponentCanvas.getGraphicsContext2D();
        for (int i = 50; i <= 500; i+=50) {
            gcP.strokeLine(i,0,i,500);
            gcP.strokeLine(0,i,500,i);
        }
        for (int i = 50; i <= 500; i+=50) {
            gcO.strokeLine(i,0,i,500);
            gcO.strokeLine(0,i,500,i);
        }

        TextArea ta = new TextArea();
        TextField tf = new TextField();


        pane.add(playerCanvas,0,0,1,1);
        pane.add(opponentCanvas,1,0,1,1);
        pane.add(tf,0,1,2,1);
        pane.add(ta,0,2,2,1);
        pane.setHgap(50);
        stage.setScene(new Scene(pane));
        stage.setTitle("BattleShip");
        stage.show();


        tf.setOnAction(e -> {
            try {
                String coordinatesToFire = tf.getText();
                toServer.writeUTF(coordinatesToFire);

                int [] coordinate = new int[2];

                coordinate[0] = Character.getNumericValue(tf.getText().charAt(0));
                coordinate[1] = Character.getNumericValue(tf.getText().charAt(1));

                toServer.flush();
                String message = fromServer.readUTF();
                boolean hit = fromServer.readBoolean();

                ta.appendText(message + "\n");
                if (hit) {
                    gcO.setFill(Color.RED);
                } else {
                    gcO.setFill(Color.BLACK);
                }
                gcO.fillOval(coordinate[0] * 50 + 20, coordinate[1] * 50 + 20, 10,10);

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
