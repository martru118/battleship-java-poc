/**
 *
 * Multithreading Test
 * Chat/log and timer syatem
 *
 */

package test;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Clock {

    // create panes
    StackPane stackPane = new StackPane();
    VBox vBox = new VBox();
    Label timerLabel;
    int time = 0;

    // Timer
    MyTimer myTimer;

    public Clock() {
        initialize();
    }

    public void initialize(){
        stackPane.getChildren().add(vBox);
        Scene scene = new Scene(stackPane,450,300);

        // create and add elements to vBox
        TextArea textArea = new TextArea();
        textArea.setText("");
        BackgroundImage backgroundimage = new BackgroundImage(new Image("ship.jpg",500,100,false,true,true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background=new Background(backgroundimage);
        //textArea.setBackground(background);
        textArea.setStyle("-fx-text-fill:#CC66AA;");
        textArea.setPromptText("Chat");
       textArea.setEditable(false);
       textArea.setWrapText(true);



        TextField textField = new TextField();
        timerLabel = new Label("0:00");
        timerLabel.setFont(new Font(30));
        Image image=new Image("clock.jpg");
        ImageView imageView=new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        timerLabel.setGraphic(imageView);
        textField.setPrefHeight(80);
        textField.setBackground(background);
        vBox.getChildren().addAll(timerLabel);

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

    }

    public StackPane getClockPane(){
        return stackPane;
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
