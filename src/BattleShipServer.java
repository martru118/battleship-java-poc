package sample;

import javafx.application.Application;
import javafx.application.Platform;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class BattleShipServer extends Application {
    boolean[][] board = new boolean[10][10];
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

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                //server started time + date
                Platform.runLater(() -> ta.appendText("Server started at " +  new Date() + '\n'));
                //socket is your clients socket
                Socket socket = serverSocket.accept();

                DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());

                while(true) {
                    String coordinateReceived = fromClient.readUTF();

                    //make string to int array
                    int [] coordinate = new int[2];

                    coordinate[0] = Character.getNumericValue(coordinateReceived.charAt(0));
                    coordinate[1] = Character.getNumericValue(coordinateReceived.charAt(1));

                    if (isHit(coordinate, board)) {
                        toClient.writeUTF("You Hit Opponent!");
                        ta.setText("You have been hit \n");
                    } else {
                        toClient.writeUTF("You have missed.");
                        ta.setText("Opponent missed \n");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();



    }
    public boolean isHit(int[] coordinates, boolean[][] board) {
        if (board[coordinates[0]][coordinates[1]]) {
            return true;
        }
        return false;
    }

    private class Ship {
        int spaces;
        String name;
        Ship(int spaces, String name){
            this.spaces = spaces;
            this.name = name;
        }


    }
}
