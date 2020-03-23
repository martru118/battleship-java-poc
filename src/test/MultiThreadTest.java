/**
 *
 * Multithreading Test
 * Chat/log and timer syatem
 *
 */

package test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class MultiThreadTest extends Application {

    // create panes
    StackPane stackPane = new StackPane();
    VBox vBox = new VBox();
    Label timerLabel;
    int time = 0;

    // Timer
    MyTimer myTimer;

    @Override
    public void start(Stage stage) throws Exception {

        stackPane.getChildren().add(vBox);
        Scene scene = new Scene(stackPane);

        // create and add elements to vBox
        TextArea textArea = new TextArea();
        textArea.setText("");
        textArea.setPromptText("Chat");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        TextField textField = new TextField();
        timerLabel = new Label("0:00");

        vBox.getChildren().addAll(timerLabel, textArea, textField);

        myTimer = new MyTimer();
        Runnable timerTask = new TaskClass1();
        Thread thread1 = new Thread(timerTask);

        thread1.start();
        //time = 0;

        // add event handling for textField
        textField.setOnAction(actionEvent -> {
            String currentText = textArea.getText();
            String newText = myTimer.getTime() + " : " +textField.getText();
            textArea.setText(currentText + '\n' + newText);
            textArea.setScrollTop(Double.MAX_VALUE);
            textField.clear();
        });

        // set stage
        stage.setScene(scene);
        stage.show();
    }

    // this task adds a second to the timer every passing second
    class TaskClass1 implements Runnable{
        @Override
        public void run() {
            while( true ) {
                myTimer.addSecond();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class MyTimer{
        private int time = 0;

        public void addSecond(){
            this.time++;
            updateTimerLabel();
        }

        public void updateTimerLabel() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    timerLabel.setText(myTimer.getMinute() + ":" + myTimer.getSecond());
                }
            });

        }

        public String getTime() { return getMinute() + ":" + getSecond(); }
        public String getMinute() { return String.valueOf( this.time / 60 ); }
        public String getSecond() {
            int second = this.time % 60;
            if ( second < 10) {
                return "0" + second;
            } else {
                return String.valueOf(second);
            }
        }
    }


}
