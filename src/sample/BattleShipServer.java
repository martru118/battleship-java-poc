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

        Thread startingThread = new Thread(() -> {
            Ship[] ships = new Ship[3];
            ships[0] = new Ship(2, "Patrol");
            ships[1] = new Ship(3,"Submarine");
            ships[2] = new Ship(3,"Destroyer");

            for (int i = 0; i < ships.length; i++) {
                ta.appendText("Welcome to Battle ship \n" +
                        "Please place your " + ships[i].name +" ("+ships[i].spaces+ " spaces) \n" );

                for(int j = 0; j < ships[i].spaces; j++) {
                    ta.appendText("Please type in coordinate #" + j+" out of "+ships[i].spaces+".\n");

                    tf.setOnAction(e -> {
                        int coordinates[] = new int[2];
                        coordinates[0] = Integer.parseInt(tf.getText(0,1));
                        coordinates[1] = Integer.parseInt(tf.getText(1,2));

                        while (board[coordinates[0]][coordinates[1]]) {
                            ta.appendText("Spot taken, try again");
                        }
                        board[coordinates[0]][coordinates[1]] = true;
                        gcP.setFill(Color.AQUA);
                        gcP.fillRect(coordinates[0]*50 + 2,coordinates[1]*50 + 2, 45,45);
                    });
                }
            }

        });

        Thread opponentTurn = new Thread(() -> {
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
                        toClient.writeBoolean(true);
                        ta.setText("You have been hit \n");
                        gcP.setFill(Color.RED);
                    } else {
                        toClient.writeUTF("You have missed.");
                        toClient.writeBoolean(false);
                        ta.setText("Opponent missed \n");
                        gcP.setFill(Color.BLACK);
                    }
                    gcP.fillOval(coordinate[0] * 50 + 20, coordinate[1] * 50 + 20, 10,10);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        startingThread.start();
        opponentTurn.start();



    }
    public boolean isHit(int[] coordinates, boolean[][] board) {
        if (board[coordinates[0]][coordinates[1]]) {
            return true;
        }
        return false;
    }
    public void welcome (TextArea ta, TextField tf) {

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
