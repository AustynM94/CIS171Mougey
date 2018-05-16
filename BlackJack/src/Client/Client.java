package Client;

import Server.Card;
import Server.Deck;
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Austyn
 */
public class Client extends Application {    
    DataOutputStream out = null;
    DataInputStream in = null;
    
    @Override
    public void start(Stage primaryStage) {
        //Panel paneForTextField to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter chat: "));
        
        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);
        
        BorderPane mainPane = new BorderPane();
        //Text area to display output from server
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);
        
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        tf.setOnAction (e -> {
            try {
                //Gets chat from text field
                int chat = Integer.parseInt(tf.getText());
                ta.appendText("You say: " + chat + "\n");
                //Sends chat to server
                out.writeInt(chat);
                out.flush();
            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        });
        
        new Thread(() -> {
            try {
                Socket socket = new Socket ("localhost", 8000);
                Platform.runLater( () ->
                    ta.appendText("Enter 5 to be dealt your hand\n"));
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                
                while (true) {
                    //Get reply from server
                    int reply = in.readInt();
                    //Display to text area
                    //A switch to check if you won, lost, tied, bust, and if none of those then you get told what your hand total is and asked if you want to draw again or stay.
                    switch (reply) {
                        case 0: ta.appendText("YOU LOSE! Enter 5 to get a new hand.\n");
                                break;
                        case -1: ta.appendText("YOU WIN! Enter 5 to get a new hand.\n");
                                break;
                        case -2: ta.appendText("DRAW! Enter 5 to get a new hand\n");
                                break;
                        case -3: ta.appendText("BUST YOU LOSE! Enter 5 to get a new hand\n");
                                break;
                        case -4: ta.appendText("TWENTY ONE! YOU WIN! Enter 5 to ge a new hand\n");
                                break;
                        default: ta.appendText("Your hand total is: " + reply + ". If your want another card enter 1, to stay enter 2" + "\n");
                    }                    
                }
            } //End of try
            catch (IOException ex) { 
                System.err.println(ex);
            }
        }).start();
    } //End of method start
    
    public static void main(String[] args) {
        Application.launch(args);
    }
} //End of class Server