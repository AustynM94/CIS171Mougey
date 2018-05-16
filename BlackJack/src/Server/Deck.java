/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Austyn
 */
public class Deck {
    
    private ArrayList<Card> cards;
    private ArrayList<Card> pulledCards;
    private Random rand;
    
    public void getValue() {
        
    }
    
    public enum Suits {
        SPADES,
        HEARTS,
        DIAMONDS,
        CLUBS;
    }
    
    public enum Ranks {
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10),
        KING(10),
        QUEEN(10),
        ACE(11);
        
        private final int cardValue;
        
        Ranks(int points) {
            this.cardValue = points;
        }
        
        public int getValue() {
            return this.cardValue;
        }
    }
    
    public void createArrays() {
        rand = new Random();
        pulledCards = new ArrayList<Card>();
        cards = new ArrayList<Card>(Suits.values().length * Ranks.values().length);
        createDeck();
    }
    
    public void createDeck() {
        cards.clear();    
        pulledCards.clear();
        
        for (Suits s : Suits.values()) {
            for (Ranks r : Ranks.values()) {
                Card c = new Card(r,s);
                cards.add(c);
            }  
        }
        Collections.shuffle(cards);
    }
    
    public static void shuffle(ArrayList<?> list) {
        Collections.shuffle(list);
    }
    
    public ArrayList<Card> getDeck(){
        return cards;
    }
    
    
    //Pulls and removes random card from deck
    public Card pullRandom() {
        if (cards.isEmpty())
            return null;

        Card res = cards.remove(randInt(0, cards.size() - 1));
        if (res != null)
            pulledCards.add(res);
        return res;
    }
    
    public int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
    public boolean isEmpty(){
        return cards.isEmpty();
    }
}


