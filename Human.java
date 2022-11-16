import java.util.ArrayList;
import java.util.*;

// -----------------------------------
// AUTHOR            : Fisayo Olofin
// CLASS          : Human
// 
// 
// REMARKS: Human Player 
// -----------------------------------

public class Human implements IPlayer {

    // Instance Variables
    private int index; // players index
    private ArrayList<Card> suspects; // list of suspects
    private ArrayList<Card> locations; // list of locations
    private ArrayList<Card> weapons; // list of weapons

    private ArrayList<Card> playerCards; // cards dealt to this player

    // Constructor
    public Human() {
        playerCards = new ArrayList<>();
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
        suspects = ppl;
        locations = places;
        this.weapons = weapons;
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
        System.out.println("You received the "+ c.getType() +" card: " + c.getValue());
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
        Scanner keyboard = new Scanner(System.in); // gets userInputs
        if(options.isEmpty()){
            System.out.println("Player " + ip.getIndex() + " asked you about '" + g + "', but you couldn't answer.");
        }
        else if(options.size() == 1){
            answer = options.get(0);
            System.out.println("Player " + ip.getIndex() + " asked you about '" + g + "', you only have one card, '" 
            + answer + "', showed it to them.");
        }
        else{
            answer = getUserCard(options, "show", keyboard);
        }

        //keyboard.close();
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
    }

    //------------------------------------------------------
    // getGuess
    //
    // PURPOSE:    get guess from a player
    // PARAMETERS:
    //          
    // Returns: guess made by player.
    //------------------------------------------------------
    public Guess getGuess() {
        System.out.println("It is your turn. ");
        Scanner keyboard = new Scanner(System.in); // gets userInputs

        Card suspect = getUserCard(suspects, "sugest", keyboard); //suspect guessed
        Card location = getUserCard(locations, "sugest", keyboard); //location guessed
        Card weapon = getUserCard(weapons, "sugest", keyboard); //weapon guessed

        boolean isAccusation = getAccusation(keyboard);

        Guess guess = new Guess(suspect, location, weapon, isAccusation);
        //keyboard.close();
        return guess;
    }// end getGuess


    //------------------------------------------------------
    // getUserCard
    //
    // PURPOSE:     helper method to ask a player what card they will like to select from a list
    //              used when making or answering a guess
    // PARAMETERS:
    //              options:    A list of possible cards
    //              type:       to distinguish when guessing or answering
    //              keyboard:   to get input from player
    // Returns: The Card select.
    //------------------------------------------------------  
    private Card getUserCard(ArrayList<Card> options, String type, Scanner keyboard){
        int cardIndex = Integer.MIN_VALUE;
        boolean valid = false;
        if(type.equals("show")){
            System.out.println("Which card do you want to show?");
        }
        else{
            System.out.println("Which " + options.get(0).getType() + " do you want to suggest?");
        }
        
            
        print(options);//print available options
        while(!valid){
            //get the user input
            try{
                cardIndex = Integer.parseInt(keyboard.nextLine());
                valid = cardIndex > 0 && cardIndex <= options.size();
            }catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Enter a valid integer");
            }
            if(!valid && cardIndex != Integer.MIN_VALUE){
                System.out.println("Enter a number greater than 0 and less than " + options.size());
            }
        }//while
        
        return options.get(cardIndex-1);
    }// end getCard




    //------------------------------------------------------
    // getAccusation
    //
    // PURPOSE:    helper method to ask a player if the guess made is an accusation or suggestion
    // PARAMETERS:
    //              keyboard: Scanner to get iniput from the user
    // Returns: 
    //------------------------------------------------------  
    private boolean getAccusation(Scanner keyboard){
        boolean isAccusation = false;
        boolean valid = false;
        while(!valid){
            System.out.println("Is this an accusation? [Y/N]");
            String userInput = keyboard.nextLine();
            valid = userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("n")
                    ||userInput.equalsIgnoreCase("yes") ||userInput.equalsIgnoreCase("no");
           
            isAccusation = userInput.equalsIgnoreCase("yes") ||userInput.equalsIgnoreCase("y");
            if(!valid){
                System.out.println("Enter a valid input [Y/N]");
            }
        }//while

        
        return isAccusation;
    }//end getAccusation


    //------------------------------------------------------
    // print
    //
    // PURPOSE:    prints a list of cards to the user.
    // PARAMETERS:
    //              list: Arraylist containing the cards
    // Returns: 
    //------------------------------------------------------    
    private void print(ArrayList<Card> list){
        for(Card card : list){
            System.out.println((list.indexOf(card) + 1 ) + ". " + card);
        }
    }


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
            System.out.println("No one could refute your suggestion.");
        }
        else{
            System.out.println("Player " + ip.getIndex() + " refuted your suggestion by showing you " + c.getValue() + ".");
        }
    }// end receiveInfo

}
