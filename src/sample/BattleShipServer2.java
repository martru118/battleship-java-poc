package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class BattleShipServer2 extends Application  {
    File filename=new File("scores.csv");

    FileWriter fileWriter;
    BufferedWriter bufferedWriter;
    TextArea ta = new TextArea();

    {
        try {
            fileWriter = new FileWriter(filename, true);
            bufferedWriter=new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String info="";

    @Override
    public void start(Stage stage) throws Exception {
        Label Usernamelabel = new Label("User Name");

        String username;


        //TextArea ta = new TextArea();

        ScrollPane sp=new ScrollPane(ta);
        GridPane gridPane = new GridPane();
        gridPane.add(Usernamelabel, 0, 0);
       gridPane.setBackground(new Background(new BackgroundImage(new Image("/sample/battleship.jpg", 500, 500, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        BorderPane borderPane = new BorderPane();
       borderPane.setCenter(gridPane);
        borderPane.setBottom(ta);

        Scene scene = new Scene(borderPane, 500, 500);
        stage.setScene(scene);
        stage.show();


        new Thread(() -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(1490);
                ta.appendText("MultiThreadServer started at "
                        + new Date() + '\n');
                while (true) {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();
                    InetAddress inetAddress = socket.getInetAddress();
                    ta.appendText("Client's host name is "
                            + inetAddress.getHostName() + "\n");

                    new Thread(new HandleAClient(socket)).start();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }



        }).start();
    }
    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket

        /** Construct a thread */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /** Run a thread */
        public void run() {
            try {
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                // Continuously serve the client
                while (true) {
                    // Receive score from the client
                    int score = inputFromClient.readInt();
                    ta.appendText(String.valueOf(score));
                    String name=inputFromClient.readUTF();
                    // save name and score
                    info=name+","+score;
                    saveincsv(info);


                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void saveincsv(String csv){
        try {
            bufferedWriter.newLine();
            bufferedWriter.write(csv);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
