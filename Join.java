public class Join {
    public Join(){
    }
    int connected = 0; // to signal to server thread how many users connect
    int ready = 0; // to signal how many players are read
    boolean Deck_distributed = false; // to signal that the deck was distributed
    int current_dist = 1; // to signal whose turn is it to take cards
    Deck deck = new Deck();

    Card table = null;
     public void DeckCreation(){
        deck.Create_Deck();
        Deck_distributed = true;
        table = deck.drawACard();
    }

    int turn = 1;

     public void play(Card c){
         table = c;
         turn = turn % 2 + 1;
     }
     
    boolean end = false;


}
