import java.util.ArrayList;
import java.util.*;

// -----------------------------------
// AUTHOR            : Fisayo Olofin
// CLASS          : Computer
// 
// 
// REMARKS: Computer Player 
// -----------------------------------


public class Computer implements IPlayer {
    // Instance Variables
    private int index; // players index

    private ArrayList<Card> playerCards; // cards dealt to this player
    private boolean validAccusation;
    private ArrayList<Card> lastSuggestion;

    private ArrayList<Card> possibleSuspects;
    private ArrayList<Card> possibleLocations;
    private ArrayList<Card> possibleWeapons;

    // Constructor
    public Computer() {
        playerCards = new ArrayList<>();
        validAccusation = false;

        lastSuggestion = new ArrayList<>();

        possibleSuspects = new ArrayList<>();
        possibleLocations = new ArrayList<>();
        possibleWeapons = new ArrayList<>();
    }


    //------------------------------------------------------
    // setUp
    //
    // PURPOSE:    receive parameters and assign to the player
    // PARAMETERS:
    //              numPlayer:  number of players in the game.
    //              index:      players seat on the table.
    //              ppl:        all the suspect cards.
    //              places:     all the location cards.
    //              weapons:    all the weapon cards
    // Returns: 
    //------------------------------------------------------
    public void setUp(int numPlayers, int index, ArrayList<Card> ppl, ArrayList<Card> places, ArrayList<Card> weapons) {
        this.index = index;

        possibleSuspects.addAll(ppl);
        possibleLocations.addAll(places);
        possibleWeapons.addAll(weapons);
    }// end setUp


    //------------------------------------------------------
    // setCard
    //
    // PURPOSE:    assign a particular card to the player
    // PARAMETERS:
    //              c:  card dealt to the player
    // Returns: 
    //------------------------------------------------------
    public void setCard(Card c) {
        playerCards.add(c);
        if(c.getType().equals("suspect")){
            possibleSuspects.remove(c);
        }
        else if(c.getType().equals("location")){
            possibleLocations.remove(c);
        }
        else if(c.getType().equals("weapon")){
            possibleWeapons.remove(c);
        }

    }// end setCard


    //------------------------------------------------------
    // getIndex
    //
    // PURPOSE:    returns current position of the player on the table
    // PARAMETERS:
    //              
    // Returns:     index
    //------------------------------------------------------
    public int getIndex() {
        return index;
    }// end getIndex


    //------------------------------------------------------
    // canAnswer
    //
    // PURPOSE:    answering the guess made by a player
    // PARAMETERS:
    //              ip:  player who made the guess.
    //              g:      guess made
    //          
    // Returns: a card in the guess or null if guess can not be answer.
    //------------------------------------------------------
    public Card canAnswer(Guess g, IPlayer ip) {
        ArrayList<Card> options = commonCards(g);
        Card answer = null;

        if(options.size() > 0){
            Collections.shuffle(options);
            answer = options.get(0);
        }

        return answer;
    }// end canAnswer

    //------------------------------------------------------
    // commonCards
    //
    // PURPOSE:    to get the common cards in guess and the current players cards
    // PARAMETERS:
    //              g:      guess made
    //          
    // Returns: an ArrayList of common cards in the guess and player cards.
    //------------------------------------------------------
    private ArrayList<Card> commonCards(Guess g){
        ArrayList<Card> options = new ArrayList<>();
        for(Card card : playerCards){
            if (card.isEqual(g.getWeapon()) || card.isEqual(g.getSuspect()) || card.isEqual(g.getLocation())){
                options.add(card);
            }
        }
        return options;
    }// end commonCards


    //------------------------------------------------------
    // getGuess
    //
    // PURPOSE:    get guess from a player
    // PARAMETERS:
    //          
    // Returns: guess made by player.
    //------------------------------------------------------    
    public Guess getGuess() {

        if(!validAccusation){
            lastSuggestion.add(0, possibleSuspects.get(0));
            lastSuggestion.add(1, possibleLocations.get(0));
            lastSuggestion.add(2, possibleWeapons.get(0));
            validAccusation = possibleSuspects.size() == 1 && possibleLocations.size() == 1 && possibleWeapons.size() == 1;
        }
        
        
        return new Guess(lastSuggestion.get(0), lastSuggestion.get(1), lastSuggestion.get(2), validAccusation);
    }// end getGuess


    //------------------------------------------------------
    // receiveInfo
    //
    // PURPOSE:    updates a player about the guess they made and who answered it
    // PARAMETERS:
    //              ip:     player that answered the guess, null if no one answered
    //              c:      card shown to refute guess
    // Returns: 
    //------------------------------------------------------
    public void receiveInfo(IPlayer ip, Card c) {
        if(c == null && ip == null){
            validAccusation = true;
            System.out.println("No one could answer your suggestion.");
        }
        else{
            if(c.getType().equals("suspect")){
                possibleSuspects.remove(c);
            }
            else if(c.getType().equals("location")){
                possibleLocations.remove(c);
            }
            else if(c.getType().equals("weapon")){
                possibleWeapons.remove(c);
            }
        }
    }// end receiveInfo



}
