import java.net.*;
import java.util.*;

import java.lang.*;
import java.io.*;

public class Deck {

    public ArrayList<Card> unplayedCards = new ArrayList<Card>();
    public Deque<Card> playedCards = new ArrayDeque<>();

    public Deck(){}
    void Create_Deck() { // 188 card
        for (Card.Color color : Card.Color.values()) {
            if (color == Card.Color.__)
                continue;

            unplayedCards.add(new Card.NumberedCard(color, 0));
            for (int j = 0; j < 2; j++) {// 2 cards each from 1-
                for (int k = 1; k < 10; k++) {
                    unplayedCards.add(new Card.NumberedCard(color, k)); // 1-9
                }
                unplayedCards.add(new Card.SpecialCard(color, Card.SpecialCardFunction.PLUSTWO)); // 2 plus two
                unplayedCards.add(new Card.SpecialCard(color, Card.SpecialCardFunction.SKIP)); // 2 skips
                unplayedCards.add(new Card.SpecialCard(color, Card.SpecialCardFunction.SUBWAY)); // 2 "subways" reverse
            }
        }
        for(int i = 0 ; i < 4 ; i++) {
            unplayedCards.add(new Card.SpecialCard(Card.Color.__, Card.SpecialCardFunction.PLUSFOUR));
            unplayedCards.add(new Card.SpecialCard(Card.Color.__, Card.SpecialCardFunction.CHANGECOLOR));
        }
        Collections.shuffle(unplayedCards);
    }

    Card drawACard() {
        if (!unplayedCards.isEmpty())
            return unplayedCards.remove(0);
        else
            return playedCards.removeFirst();
    }

    ArrayList<Card> initialDraw(Deck this){
        ArrayList<Card> a = new ArrayList<Card>();
        for(int i = 0 ; i < 7 ; i++)
            a.add(this.drawACard());
        return a;
    }
}
