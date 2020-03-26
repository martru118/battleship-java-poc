package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BattleShipClient2 extends Application {
    private TextArea ta = new TextArea();
    private TextField tf = new TextField();
    private final String path = "src/scores.csv";

    //boards
    private boolean[][] playerBoard = new boolean[10][10];
    private boolean[][] opponentBoard = new boolean[10][10];
    //to keep track of the grids which have been discovered
    private boolean[][] disclosedboardPlayer=new boolean[10][10];
    private boolean [][] disclosedboardopponent=new boolean[10][10];
    //position what have been hit
    private boolean[][] hitboardPlayer=new boolean[10][10];
    private boolean [][] hitboardopponent=new boolean[10][10];
    //position what have been missed
    private boolean[][] missboardPlayer=new boolean[10][10];
    private boolean [][] missboardopponent=new boolean[10][10];
    //to take the coordinates from the user and the opponent and initialising it to something invalid.
    int[] coordinate={10,10};
    //to make sure what input length is put by the user.
    int inputlength=0;
    String userinput="";
    public int playerscore;
    public int opponentscore;

    //game saving
    String filename="current game.txt";
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
    private boolean playerTurn = true;
    @Override
    public void start(Stage stage) throws Exception {

        //set stage
        Button startGame = new Button("START THE GAME");
        Button exitGame = new Button("EXIT THE GAME");
        Button resumegame = new Button("RESUME THE PREVIOUS GAME");
        tf.setPromptText("Enter the Column Number and then the Row number (eg. 74)");
        pane.add(playerCanvas, 0, 0);
        pane.add(opponentCanvas, 1, 0);
        pane.add(tf, 0, 1, 2, 1);
        pane.add(ta, 0, 4, 2, 1);
        pane.add(startGame, 0, 2, 2, 1);
        pane.add(resumegame, 1, 2, 2, 1);
        pane.add(exitGame, 0, 3, 2, 1);
        pane.setHgap(50);
        stage.setScene(new Scene(pane));
        stage.setTitle("BattleShip");
        stage.show();

        //set boards
        AIboard();
        playerBoard();

        //draw grid
        pane.setMinHeight(600);
        for (int i = 50; i <= 500; i += 50) {
            gcP.strokeLine(i, 0, i, 500);
            gcP.strokeLine(0, i, 500, i);

            gcP.strokeText(String.valueOf(i / 50), i, 15);
            gcP.strokeText(String.valueOf(i / 50 - 1), 3, i);

            gcO.strokeLine(i, 0, i, 500);
            gcO.strokeLine(0, i, 500, i);

            gcO.strokeText(String.valueOf(i / 50), i, 15);
            gcO.strokeText(String.valueOf(i / 50 - 1), 3, i);
        }


        //play game

        resumegame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    load();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        startGame.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                while (true) {
                    Image imagewater=new Image("/sample/water.jpg");
                    Image imagefire=new Image("/sample/fire.PNG");
                    String fire="fire.mp3";
                    String water="water.mp3";
                    Media soundfire = new Media(new File(fire).toURI().toString());
                    MediaPlayer mediafire = new MediaPlayer(soundfire);
                    //mediafire.play();
                    Media soundwater = new Media(new File(water).toURI().toString());
                    MediaPlayer mediawater = new MediaPlayer(soundwater);
                    TextInputDialog tid = new TextInputDialog("");
                    tid.setHeaderText("Enter the Column Number and then the Row number (eg. 74)");

                    final Button cancel = (Button) tid.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancel.addEventFilter(ActionEvent.ACTION, event ->
                            tid.showAndWait()
                    );

                    //player turn
                    if (getPlayerturn()) {

                /*
                tid.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d{0,1}([\\,]\\d{0,1})?"))
                        tid.getEditor().setText(oldValue);
                });
                 */
                        while (!isValidMove(coordinate, disclosedboardopponent) || inputlength != 2) {
                            Optional<String> c = tid.showAndWait();

                            c.ifPresent(coordinates -> {
                                coordinate = convertToInt(coordinates);
                                inputlength = coordinates.length();
                            });
                        }



                        if (isHit(coordinate, opponentBoard)) {

                            //gcO.setFill(Color.RED);
                            gcO.drawImage(imagefire,coordinate[0] * 50 + 2, coordinate[1] * 50 + 2, 45, 45);
                            mediafire.play();
                            playerscore = playerscore + 25;
                            opponentscore = opponentscore - 25;
                            hitboardopponent[coordinate[0]][coordinate[1]]=true;
                            ta.appendText("You have Hit opponent.\n" + "Your score is " + playerscore + ".Opponent's score is " + opponentscore+"\n");

                        } else {
                            missboardopponent[coordinate[0]][coordinate[1]]=true;
                            ta.appendText("You have missed. \n" + "Your score is " + playerscore + ".Opponent's score is " + opponentscore+"\n");
                            //gcO.setFill(Color.BLACK);
                            mediawater.play();
                            gcO.drawImage(imagewater,coordinate[0] * 50 + 2, coordinate[1] * 50 + 2, 45, 45);

                        }
                        disclosedboardopponent[coordinate[0]][coordinate[1]] = true;
                        //isValidMove()



                /*
                f.setOnAction(e -> {
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

                        changeTurn();
                    } else {
                        //computer turn

                        Random r = new Random();
                        int[] coordinate2 = new int[2];
                        coordinate2[0] = r.nextInt(10);
                        coordinate2[1] = r.nextInt(10);
                        while(!isValidMove(coordinate2,disclosedboardPlayer)) {

                            coordinate2[0] = r.nextInt(10);
                            coordinate2[1] = r.nextInt(10);
                        }
                        if (isHit(coordinate2, playerBoard)) {

                            //gcP.setFill(Color.RED);
                            playerscore = playerscore - 25;
                            opponentscore = opponentscore + 25;
                            hitboardPlayer[coordinate2[0]][coordinate2[1]] = true;
                            gcP.drawImage(imagefire,coordinate2[0] * 50 + 2, coordinate2[1] * 50 + 2, 45, 45);
                            mediafire.play();
                            ta.appendText("You have been HIT! \n " + "Your score is " + playerscore + ".Opponent's score is " + opponentscore+"\n");

                        } else {
                            ta.appendText("Opponent has missed. \n" + "Your score is " + playerscore + ".Opponent's score is " + opponentscore+"\n");
                            gcP.setFill(Color.BLACK);
                            mediawater.play();
                            gcP.drawImage(imagewater,coordinate2[0] * 50 + 2, coordinate2[1] * 50 + 2, 45, 45);
                            missboardPlayer[coordinate2[0]][coordinate2[1]] = true;

                        }
                        //gcP.fillOval(coordinate2[0] * 50 + 20, coordinate2[1] * 50 + 20, 10, 10);
                        disclosedboardPlayer[coordinate2[0]][coordinate2[1]] = true;

                        changeTurn();
                    }


                    //check who has lost
                    if (hasLost(opponentBoard)) {
                        ta.appendText("You have won! \n" + "Your score is " + playerscore + ".Opponent's score is " + opponentscore + "\n");
                        break;
                    } else if (hasLost(playerBoard)) {
                        ta.appendText("You have lost. \n" + "Your score is " + playerscore + ".Opponent's score is " + opponentscore + "\n");
                        break;
                    }

                }
            }
        });

        exitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    save();
                    load();
                } catch (Exception e) {
                    e.printStackTrace();}
                System.exit(0);
            }
        });
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



    public boolean isValidMove ( int[] coordinate, boolean[][] discloseboard){
        if (coordinate[1] > 9||coordinate[0]>9){
            //ta.appendText("Please Type a different coordinate.This coordinate is out of board");
            return false;}
        else if(discloseboard[coordinate[0]][coordinate[1]]){
            //ta.appendText("Please Type a different coordinate.This grid is already discovered");
            return false;}

        return true;
    }
    //returns true if ship is able to be placed on board
    public boolean isValid (Ship ship,int[] coordinate, boolean[][] board){

        if(coordinate[0] > 9 || coordinate[1] > 9){
            return false;
        }
        //if the space is occupied
        if (board[coordinate[0]][coordinate[1]]){
            return false;
        }

        return true;
    }

    public boolean isValid (Ship ship,int[][] coordinates, boolean[][] board){
        for (int[] coord : coordinates) {
            if (!isValid(ship, coord, board))
                return false;
        }
        return true;
    }

    public void placeShips (Ship ship,boolean[][] board, int[][] coordinate){
        for (int[] coords : coordinate) {
            board[coords[0]][coords[1]] = true;
            Image imageship=new Image("/sample/ship.PNG");
            if (board == playerBoard) {
                gcP.setFill(Color.AQUA);
                //gcP.fillRect(coords[0] * 50 + 2, coords[1] * 50 + 2, 45, 45);
                gcP.drawImage(imageship,coords[0] * 50 + 2, coords[1] * 50 + 2, 45, 45);
            }
        }
    }

    public void AIboard () {
        for (int i = 0; i < ships.length; i++) {
            Random r = new Random();

            int[][] c = new int[ships[i].getSpaces()][2];
            c[0][0] = r.nextInt(10); //number from 0-9
            c[0][1] = r.nextInt(10);

            //50% chance of ships being placed vertically or horizontally
            if (r.nextInt() % 2 ==0){
                //horizontal placement
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0] + 1;
                    c[j][1] = c[j-1][1];
                }
            } else {
                //vertical placement
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0];
                    c[j][1] = c[j-1][1] + 1;
                }
            }

            //check if position is valid
            if (isValid(ships[i], c, opponentBoard)) {
                placeShips(ships[i], opponentBoard, c);
                ships[i].setCoordinates(c);
            } else {
                i--;
            }
        }
    }

    public boolean isHit ( int[] coordinate, boolean[][] board){
        if (board[coordinate[0]][coordinate[1]]) {
            board[coordinate[0]][coordinate[1]] = false;
            return true;
        }
        return false;
    }

    public int[] convertToInt (String s){
        int[] coordinates = new int[2];
        coordinates[0] = Character.getNumericValue(s.charAt(0));
        coordinates[1] = Character.getNumericValue(s.charAt(1));
        return coordinates;
    }

    public boolean hasLost ( boolean[][] board){
        for (boolean[] coords : board) {
            for (int j = 0; j < board.length; j++) {
                if (coords[j])
                    return false;
            }
        }

        return true;
    }

    public void playerBoard () {
        for (int i = 0; i < ships.length; i++) {
            Random r = new Random();

            int[][] c = new int[ships[i].getSpaces()][2];
            c[0][0] = r.nextInt(10); //number from 0-9
            c[0][1] = r.nextInt(10);

            //50% chance of ships being placed vertically or horizontally
            if (r.nextInt() % 2 ==0){
                //horizontal placement
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0] + 1;
                    c[j][1] = c[j-1][1];
                }
            } else {
                //vertical placement
                for (int j = 1; j < c.length; j++) {
                    c[j][0] = c[j-1][0];
                    c[j][1] = c[j-1][1] + 1;
                }
            }

            //check if position is valid
            if (isValid(ships[i], c, playerBoard)) {
                placeShips(ships[i], playerBoard, c);
                ships[i].setCoordinates(c);
            } else {
                i--;
            }
        }
    }
    public boolean getPlayerturn () {
        return playerTurn;
    }
    public void changeTurn () {
        playerTurn = !playerTurn;
    }

    //https://www.baeldung.com/java-csv-file-array
    public List<List<String>> getHighScores(String filePath) throws Exception {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();  //skip header line

            //read contents of file
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }

            //sort array
            Collections.sort(records, new Comparator<List<String>> () {
                @Override
                public int compare(List<String> current, List<String> other) {
                    if (Integer.parseInt(current.get(1)) > Integer.parseInt(other.get(1)))
                        return -1;
                    else
                        return 1;
                }
            });
            return records;
        }
    }

    //https://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
    public void writeScores(String filePath) throws Exception {
        Score newScore;     //generate new score

        //check who won
        if (getPlayerturn())
            newScore = new Score("Player", playerscore);
        else
            newScore = new Score("Computer", opponentscore);

        //write to file
        FileWriter writer = new FileWriter(filePath);
        try {
            writer.append("player,score\n");
            writer.append(newScore.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() throws Exception {
        Writer writer=null;
        String state_of_board="";
        try{
            //different line in the file different board.
            writer=new BufferedWriter(new FileWriter(filename));
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    state_of_board=String.valueOf(i)+","+String.valueOf(j)+","+String.valueOf(playerBoard[i][j]+",");
                    writer.write(state_of_board);
                }
            }
            writer.write("\n");
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    state_of_board=String.valueOf(i)+","+String.valueOf(j)+","+String.valueOf(opponentBoard[i][j]+",");
                    writer.write(state_of_board);
                }
            }
            writer.write("\n");
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    state_of_board=String.valueOf(i)+","+String.valueOf(j)+","+String.valueOf(hitboardPlayer[i][j]+",");
                    writer.write(state_of_board);
                }
            }
            writer.write("\n");
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    state_of_board=String.valueOf(i)+","+String.valueOf(j)+","+String.valueOf(hitboardopponent[i][j]+",");
                    writer.write(state_of_board);
                }
            }
            writer.write("\n");
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    state_of_board=String.valueOf(i)+","+String.valueOf(j)+","+String.valueOf(missboardPlayer[i][j]+",");
                    writer.write(state_of_board);
                }
            }
            writer.write("\n");
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    state_of_board=String.valueOf(i)+","+String.valueOf(j)+","+String.valueOf(missboardopponent[i][j]+",");
                    writer.write(state_of_board);
                }
            }
            writer.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            writer.flush();
            writer.close();
        }
    }
    public void load() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String[] entries = new String[300];
        String[] bool = new String[100];
        int index = 0;
        try {
            //this string has columnindex,rowindex and value in that index of just the first line
            String specific_line_text = Files.readAllLines(Paths.get(filename)).get(0);
            System.out.println(specific_line_text);

            entries = specific_line_text.split(",", -1);
            //loading the player board
            for (int i = 0; i < entries.length; i++) {
                try {
                    playerBoard[Integer.valueOf(entries[i])][Integer.valueOf(entries[++i])] = Boolean.valueOf(entries[++i]);
                } catch (NumberFormatException e) {
                    System.out.println("");
                }
            }
            Image imageship = new Image("/sample/ship.PNG");

            cleanBoard(playerBoard,gcP);
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (playerBoard[j][k]) {
                        gcP.drawImage(imageship, j * 50 + 2, k * 50 + 2, 45, 45);
                    }
                }
            }
            //loading the opponent board
            specific_line_text = Files.readAllLines(Paths.get(filename)).get(1);


            entries = specific_line_text.split(",", -1);
            cleanBoard(opponentBoard,gcO);
            for (int i = 0; i < entries.length; i++) {
                try {
                    opponentBoard[Integer.valueOf(entries[i])][Integer.valueOf(entries[++i])] = Boolean.valueOf(entries[++i]);
                } catch (NumberFormatException e) {
                    System.out.println("");
                }
            }
            //loading the hit board for player
            specific_line_text = Files.readAllLines(Paths.get(filename)).get(2);


            entries = specific_line_text.split(",", -1);

            for (int i = 0; i < entries.length; i++) {
                try {
                    hitboardPlayer[Integer.valueOf(entries[i])][Integer.valueOf(entries[++i])] = Boolean.valueOf(entries[++i]);
                } catch (NumberFormatException e) {
                    System.out.println("jbj");
                }
            }
            Image imagefire = new Image("/sample/fire.PNG");


            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (hitboardPlayer[j][k]) {
                        gcP.drawImage(imagefire, j * 50 + 2, k * 50 + 2, 45, 45);
                    }
                }
            }
            specific_line_text = Files.readAllLines(Paths.get(filename)).get(3);


            entries = specific_line_text.split(",", -1);

            for (int i = 0; i < entries.length; i++) {
                try {
                    hitboardopponent[Integer.valueOf(entries[i])][Integer.valueOf(entries[++i])] = Boolean.valueOf(entries[++i]);
                } catch (NumberFormatException e) {
                    System.out.println("jbj");
                }
            }


            //cleanBoard(playerBoard,gcP);
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (hitboardopponent[j][k]) {
                        gcO.drawImage(imagefire, j * 50 + 2, k * 50 + 2, 45, 45);
                    }
                }
            }
            specific_line_text = Files.readAllLines(Paths.get(filename)).get(4);


            entries = specific_line_text.split(",", -1);

            for (int i = 0; i < entries.length; i++) {
                try {
                    missboardPlayer[Integer.valueOf(entries[i])][Integer.valueOf(entries[++i])] = Boolean.valueOf(entries[++i]);
                } catch (NumberFormatException e) {
                    System.out.println("");
                }
            }
            Image imagewater = new Image("/sample/water.jpg");


            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (missboardPlayer[j][k]) {
                        gcP.drawImage(imagewater, j * 50 + 2, k * 50 + 2, 45, 45);
                    }
                }
            }
            specific_line_text = Files.readAllLines(Paths.get(filename)).get(5);


            entries = specific_line_text.split(",", -1);

            for (int i = 0; i < entries.length; i++) {
                try {
                    missboardopponent[Integer.valueOf(entries[i])][Integer.valueOf(entries[++i])] = Boolean.valueOf(entries[++i]);
                } catch (NumberFormatException e) {
                    System.out.println("");
                }
            }



            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (missboardopponent[j][k]) {
                        gcO.drawImage(imagewater, j * 50 + 2, k * 50 + 2, 45, 45);
                    }
                }
            }


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } finally {
            br.close();
        }


    }

    public void cleanBoard(boolean[][] board, GraphicsContext gc) {
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                gc.setFill(Color.WHITE);
                gc.fillRect(i * 50 + 2, k * 50 + 2, 45, 45);
            }
        }
        for (int i = 50; i <= 500; i += 50) {
            gc.strokeText(String.valueOf(i / 50), i, 15);
            gc.strokeText(String.valueOf(i / 50 - 1), 3, i);
        }
    }
}