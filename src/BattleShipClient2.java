import com.sun.jdi.event.MonitorWaitedEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.Random;

public class BattleShipClient2 extends Application {
    TextArea ta = new TextArea();
    TextField tf = new TextField();

    //boards
    boolean[][] playerBoard = new boolean[10][10];
    boolean[][] opponentBoard = new boolean[10][10];

    //io streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    //create board graphics
    GridPane pane = new GridPane();
    Canvas playerCanvas = new Canvas(500,500);
    Canvas opponentCanvas = new Canvas(500,500);
    GraphicsContext gcP = playerCanvas.getGraphicsContext2D();
    GraphicsContext gcO = opponentCanvas.getGraphicsContext2D();

    //create ships
    Ship destroyer = new Ship(4,"Destroyer");
    Ship battleship = new Ship(3,"Battleship");
    Ship patrol = new Ship(2,"Patrol");
    Ship submarine = new Ship(3,"Submarine");
    Ship carrier = new Ship(5,"Carrier");
    @Override
    public void start(Stage stage) throws Exception {
        //draw board
        pane.setMinHeight(600);
        for (int i = 50; i <= 500; i+=50) {
            gcP.strokeLine(i,0,i,500);
            gcP.strokeLine(0,i,500,i);
        }
        for (int i = 50; i <= 500; i+=50) {
            gcO.strokeLine(i,0,i,500);
            gcO.strokeLine(0,i,500,i);
        }



        //set stage
        pane.add(playerCanvas,0,0,1,1);
        pane.add(opponentCanvas,1,0,1,1);
        pane.add(tf,0,1,2,1);
        pane.add(ta,0,2,2,1);
        pane.setHgap(50);
        stage.setScene(new Scene(pane));
        stage.setTitle("BattleShip");
        stage.show();

        //set boards
        AIboard();
        playerBoard();

        //play game
        boolean playerTurn = false;
        while(true) {
            if (playerTurn) {

                TextInputDialog tid = new TextInputDialog();
                Optional<String> c = tid.showAndWait();
                c.ifPresent(coordinates -> {
                    int[] coordinate = convertToInt(coordinates);
                    if (isHit(coordinate, opponentBoard)) {
                        ta.setText("You have hit opponent! \n");
                        gcO.setFill(Color.RED);
                    } else {
                        ta.setText("You have missed. \n");
                        gcO.setFill(Color.BLACK);
                    }
                    gcO.fillOval(coordinate[0] * 50 + 20, coordinate[1] * 50 + 20, 10,10);
                });

//                tf.setOnAction(e -> {
//                    int[] coordinate = convertToInt(tf.getText());
//                     if (isHit(coordinate, opponentBoard)) {
//                        ta.setText("You have hit opponent! \n");
//                        gcO.setFill(Color.RED);
//                    } else {
//                        ta.setText("You have missed. \n");
//                        gcO.setFill(Color.BLACK);
//                    }
//                    gcO.fillOval(coordinate[0] * 50 + 20, coordinate[1] * 50 + 20, 10,10);
//                    tf.setText("");
//                });
                playerTurn = false;

            } else {
                Random r = new Random();
                int[] coordinate = new int[2];
                coordinate[0] = r.nextInt(10);
                coordinate[1] = r.nextInt(10);
                if (isHit(coordinate, playerBoard)) {
                    ta.setText("You have been HIT! \n");
                    gcP.setFill(Color.RED);
                } else {
                    ta.setText("Opponent has missed. \n");
                    gcP.setFill(Color.BLACK);
                }
                gcP.fillOval(coordinate[0] * 50 + 20, coordinate[1] * 50 + 20, 10,10);
                playerTurn = true;
            }
            if (hasLost(opponentBoard)){
                ta.appendText("You have won!");
                break;
            }
            else if (hasLost(playerBoard)) {
                ta.appendText("You have lost.");
                break;
            }
        }



        //connect to server
//        try {
//            Socket socket = new Socket("localhost",8000);
//
//            //create an input stream to send data to the server
//            fromServer = new DataInputStream(socket.getInputStream());
//
//            //create an output stream to send data to the server
//            toServer = new DataOutputStream(socket.getOutputStream());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        tf.setOnAction(e -> {
            int[] coordinate = convertToInt(tf.getText());
            if (isHit(coordinate, opponentBoard)) {
                ta.setText("You have hit opponent! \n");
                gcO.setFill(Color.RED);
            } else {
                ta.setText("You have missed. \n");
                gcO.setFill(Color.BLACK);
            }
            gcO.fillOval(coordinate[0] * 50 + 20, coordinate[1] * 50 + 20, 10,10);
            tf.setText("");
        });


    }
    //returns true if ship is able to be placed on board
    public boolean isValid(Ship ship, int[] coordinate, boolean[][] board) {
        if (board[coordinate[0]][coordinate[1]]) {
            //if ship is already there placement is not valid
            return false;
        }
        if(coordinate[1] > 9) {
            return false;
        }
        if (coordinate[0] + ship.spaces > 9) {
            return false;
        }

        return true;
    }
    public boolean isValid(Ship ship, int[][]coordinates, boolean[][] board) {
        for (int i = 0; i < coordinates.length; i++) {
            if (!isValid(ship, coordinates[i], board)) {
                return false;
            }
        }
        return true;
    }
    public void placeShips(Ship ship, boolean[][] board, int[][] coordinate) {
        for (int i = 0; i < coordinate.length;i++) {
            board[coordinate[i][0]][coordinate[i][1]] = true;
            if (board == playerBoard) {
                gcP.setFill(Color.AQUA);
                gcP.fillRect(coordinate[i][0] *50+2,coordinate[i][1] *50+2,45,45);
            } else {
                gcO.setFill(Color.AQUA);
                gcO.fillRect(coordinate[i][0] *50+2,coordinate[i][1] *50+2,45,45);
            }

        }
    }
    public void AIboard() {
        //place ships in array
        Ship[] ships = new Ship[5];
        ships[0] = destroyer;
        ships[1] = battleship;
        ships[2] = patrol;
        ships[3] = submarine;
        ships[4] = carrier;
        for (int i = 0; i < ships.length; i++) {
            Random r = new Random();
            int[][] c = new int[ships[i].spaces][2];
            c[0][0] = r.nextInt(10); //number from 0-9
            c[0][1] = r.nextInt(10);
            if(isValid(ships[i],c[0],opponentBoard)) {
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0] + 1;
                    c[j][1] = c[j-1][1];
                }
            } else {
                i--;
                continue;
            }
            if (isValid(ships[i],c,opponentBoard)) {
                placeShips(ships[i], opponentBoard,c);
                ships[i].setCoordinates(c);
            }
            else {
                i--;
            }

        }

    }
    public boolean isHit(int[] coordinate, boolean[][] board) {
        if (board[coordinate[0]][coordinate[1]]) {
            board[coordinate[0]][coordinate[1]] = false;
            return true;
        }
        return false;
    }
    public int[] convertToInt(String s) {
        int[] coordinates = new int[2];
        coordinates[0] = Character.getNumericValue(s.charAt(0));
        coordinates[1] = Character.getNumericValue(s.charAt(1));

        return coordinates;
    }
    public boolean hasLost(boolean[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j =0; j < board.length; j++) {
                if (board[i][j] == true) {
                    return false;
                }
            }
        }
        return true;
    }
    public void playerBoard() {
        Ship[] ships = new Ship[5];
        ships[0] = destroyer;
        ships[1] = battleship;
        ships[2] = patrol;
        ships[3] = submarine;
        ships[4] = carrier;

        for (int i = 0; i < ships.length; i++) {
            Random r = new Random();
            int[][] c = new int[ships[i].spaces][2];
            c[0][0] = r.nextInt(10); //number from 0-9
            c[0][1] = r.nextInt(10);
            if(isValid(ships[i],c[0],playerBoard)) {
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0] + 1;
                    c[j][1] = c[j-1][1];
                }
            } else {
                i--;
                continue;
            }
            if (isValid(ships[i],c,playerBoard)) {
                placeShips(ships[i], playerBoard,c);
                ships[i].setCoordinates(c);
            }
            else {
                i--;
            }

        }

    }

}
