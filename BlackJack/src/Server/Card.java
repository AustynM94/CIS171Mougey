/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

//import Server.Deck.Ranks;

import Server.Deck.Ranks;
import Server.Deck.Suits;

//import Server.Deck.Suits;

/**
 *
 * @author Austyn
 */
public class Card {
    private Suits suit;
    private Ranks rank;
    
    public Card(Ranks rank, Suits suit) {
        this.suit = suit;
        this.rank = rank;
    }
    
    public Enum getSuit() {
        return suit;    
    }
    
    public int getRank() {
        return rank.getValue();
    }
    
    public void setSuit(Suits suit) {
        this.suit = suit;
    }
    
    public void setRank(Ranks rank) {
        this.rank = rank;
    }
    
    @Override
    public String toString() {
        return "\n"+ rank + " of "+ suit;
    }

}
