package Server;

import Server.Deck.Ranks;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Austyn
 */
public class Server extends Application {
    Deck deck = new Deck();
    Card card;
    int value;
    int dealerValue;
    int playerValue;
    boolean gameStart = true;
    int whoWins;
    
    DataOutputStream out = null;
    DataInputStream in = null;
    
    public void pullCard() {
        card = deck.pullRandom();
        getCardValue(card);
    }
    
    public int getCardValue(Card card) {
        value = card.getRank();
        return value;
    }
    
    public int getPlayerValue() {
        playerValue += value;
        return playerValue;
    }
    
    public int getPlayerHand() {
        pullCard();
        playerValue += value;
        pullCard();
        playerValue += value;
        return playerValue;
    }
    
    public int getDealerHand() {
        pullCard();
        dealerValue += value;
        pullCard();
        dealerValue += value;
        return dealerValue;
    }
    
    //Checks to see who wins and returns a number to be sent to the client to show who won
    public int whoWins() {
        if (dealerValue > playerValue) {
            whoWins = 0;
            return whoWins;
        }
        if (playerValue > dealerValue) {
            whoWins = -1;
            return whoWins;
        }
        if (playerValue == dealerValue) {
            whoWins = -2;
            return whoWins;
        } else return 999;
    }
    
    public void checkForBust() {
        if (playerValue > 21) {
            gameStart = true;
            playerValue = -3;
        }
    }
    
    public void checkForTwentyOne() {
        if (playerValue == 21) {
            gameStart = true;
            playerValue = -4;
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        //Creates the deck to be played with
        deck.createArrays();
        
        BorderPane mainPane = new BorderPane();
        //Text area to display output from server
        TextArea ta = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        new Thread ( () -> {
            try {
                //Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater( () -> 
                    ta.appendText("Server started at " + new Date() + "\n"));
                //Listen for connection request
                Socket socket = serverSocket.accept();
                //Create data input and output streams
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                
                while (true) {
                    //Get reply from client
                    int reply = in.readInt();
 
                    Platform.runLater(() -> {
                        ta.appendText("Client says: " + reply + "\n");
                        switch (reply) {
                            //The player chose to be dealt a new card
                            case 1: if (gameStart == false) {
                                    pullCard(); 
                                    getPlayerValue();
                                    checkForBust();
                                    checkForTwentyOne();
                                    try {
                                        out.writeInt(playerValue);
                                        out.flush();
                                    }
                                    catch (IOException ex) {
                                        System.err.println(ex);
                                    }
                                }
                                    break;
                            //The player stays and the Dealer gets dealt his hand to compare who will win
                            case 2: if (gameStart == false) {
                                    try {
                                        getDealerHand();
                                        out.writeInt(whoWins());
                                        out.flush();
                                        playerValue = 0;
                                        dealerValue = 0;
                                        gameStart = true;
                                    } catch (IOException ex) {
                                        System.err.println(ex);
                                    }
                                }
                                    break;
                            //This asks if the player wants to start a new hand and is only chooseable after a hand is finished
                            case 5: if (gameStart == true) {
                                        getPlayerHand();
                                        checkForTwentyOne();
                                        try {
                                            out.writeInt(playerValue);
                                            out.flush();
                                        } catch (IOException ex) {
                                            System.err.println(ex);
                                            }
                                        gameStart = false;
                                        } 
                                        break;
                            } //End of switch
                    }); //End of runLater
                }
            } //End of try
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start(); //End of thread
    } //End of start
    

    
    public static void main(String[] args) {
        Application.launch(args);
    }
}


