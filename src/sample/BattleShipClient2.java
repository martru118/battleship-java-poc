package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Optional;
import java.util.Random;

public class BattleShipClient2 extends Application {
    private TextArea ta = new TextArea();
    private TextField tf = new TextField();

    //boards
    private boolean[][] playerBoard = new boolean[10][10];
    private boolean[][] opponentBoard = new boolean[10][10];

    //io streams
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    //create board graphics
    private GridPane pane = new GridPane();
    private Canvas playerCanvas = new Canvas(500,500);
    private Canvas opponentCanvas = new Canvas(500,500);
    private GraphicsContext gcP = playerCanvas.getGraphicsContext2D();
    private GraphicsContext gcO = opponentCanvas.getGraphicsContext2D();

    //create ships
    private Ship destroyer = new Ship(4,"Destroyer");
    private Ship battleship = new Ship(3,"Battleship");
    private Ship patrol = new Ship(2,"Patrol");
    private Ship submarine = new Ship(3,"Submarine");
    private Ship carrier = new Ship(5,"Carrier");
    private final Ship[] ships = {destroyer, battleship, patrol, submarine, carrier};

    @Override
    public void start(Stage stage) throws Exception {
        //draw grid
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
            //player turn
            if (playerTurn) {
                TextInputDialog tid = new TextInputDialog();
                tid.setHeaderText("Enter a coordinate");
                /*
                tid.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d{0,1}([\\,]\\d{0,1})?"))
                        tid.getEditor().setText(oldValue);
                });

                 */

                Optional<String> c = tid.showAndWait();

                c.ifPresent(coordinates -> {
                    if (coordinates.length() == 2) {
                        int[] coordinate = convertToInt(coordinates);
                        if (isHit(coordinate, opponentBoard)) {
                            ta.setText("You have hit opponent! \n");
                            gcO.setFill(Color.RED);
                        } else {
                            ta.setText("You have missed. \n");
                            gcO.setFill(Color.BLACK);
                        }
                        gcO.fillOval(coordinate[0] * 50 + 20, coordinate[1] * 50 + 20, 10,10);
                    }
                });

                /*
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
                */

                playerTurn = false;
            } else {    //computer turn
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

            //check who has lost
            if (hasLost(opponentBoard)){
                ta.appendText("You have won!");
                break;
            } else if (hasLost(playerBoard)) {
                ta.appendText("You have lost.");
                break;
            }
        }

        /*connect to server
        try {
            Socket socket = new Socket("localhost",8000);

            //create an input stream to send data to the server
            fromServer = new DataInputStream(socket.getInputStream());

            //create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }   */

        // user enters a coordinates to fire
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
            //tf.setText("");
        });
    }

    //returns true if ship is able to be placed on board
    public boolean isValid(Ship ship, int[] coordinate, boolean[][] board) {
        //if the space is occupied
        if (board[coordinate[0]][coordinate[1]])
            return false;
        if(coordinate[1] > 9)
            return false;
        if (coordinate[0] + ship.getSpaces() > 9)
            return false;

        return true;
    }

    public boolean isValid(Ship ship, int[][]coordinates, boolean[][] board) {
        for (int[] coord : coordinates) {
            if (!isValid(ship, coord, board))
                return false;
        }
        return true;
    }

    public void placeShips(Ship ship, boolean[][] board, int[][] coordinate) {
        for (int[] coords : coordinate) {
            board[coords[0]][coords[1]] = true;

            if (board == playerBoard) {
                gcP.setFill(Color.AQUA);
                gcP.fillRect(coords[0] * 50 + 2, coords[1] * 50 + 2, 45, 45);
            } else {
                gcO.setFill(Color.AQUA);
                gcO.fillRect(coords[0] * 50 + 2, coords[1] * 50 + 2, 45, 45);
            }
        }
    }

    public void AIboard() {
        for (int i = 0; i < ships.length; i++) {
            Random r = new Random();

            int[][] c = new int[ships[i].getSpaces()][2];
            c[0][0] = r.nextInt(10); //number from 0-9
            c[0][1] = r.nextInt(10);

            //place ships
            if(isValid(ships[i],c[0],opponentBoard)) {
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0] + 1;
                    c[j][1] = c[j-1][1];
                }
            } else {
                i--;
                continue;
            }

            //check if position is valid
            if (isValid(ships[i],c,opponentBoard)) {
                placeShips(ships[i], opponentBoard,c);
                ships[i].setCoordinates(c);
            } else {
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
        for (boolean[] coords : board) {
            for (int j = 0; j < board.length; j++) {
                if (coords[j])
                    return false;
            }
        }

        return true;
    }

    public void playerBoard() {
        for (int i = 0; i < ships.length; i++) {
            Random r = new Random();

            int[][] c = new int[ships[i].getSpaces()][2];
            c[0][0] = r.nextInt(10); //number from 0-9
            c[0][1] = r.nextInt(10);

            //place ship
            if(isValid(ships[i],c[0],playerBoard)) {
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0] + 1;
                    c[j][1] = c[j-1][1];
                }
            } else {
                i--;
                continue;
            }

            //check if placement is valid
            if (isValid(ships[i],c,playerBoard)) {
                placeShips(ships[i], playerBoard,c);
                ships[i].setCoordinates(c);
            } else {
                i--;
            }
        }
    }
}
