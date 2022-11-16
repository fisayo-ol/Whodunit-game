import java.util.*;

// -----------------------------------
// NAME            : Fisayo Olofin
// COURSE          : COMP 2150
// ASSIGNMENT      : Assignment 3
//
// -----------------------------------

public class Main {
    //names used for the game simulation
    private static final String[] SUSPECTS = {"Thanos", "Loki", "Kang", "Ultron", "Agatha", "Mysterio", "Killmonger"};
    private static final String[] LOCATIONS = {"Titan", "Asgard", "Knowhere", "Wakanda", "Xander", "Sokovia", "Nidavellir" };
    private static final String[] WEAPONS = {"The Tesseract", "Loki's Scepter", "The Darkhold", "The Infinity Gaunlet", "Mjolnir", "Strom Breaker"};

    public static void main(String[] args) {
        // location to store Cards
        ArrayList<Card> suspects = new ArrayList<>();
        ArrayList<Card> locations = new ArrayList<>();
        ArrayList<Card> weapons = new ArrayList<>();

        //Creating the cards
        addCards(CardType.SUSPECT, suspects, SUSPECTS); 
        addCards(CardType.LOCATION, locations, LOCATIONS);
        addCards(CardType.WEAPON, weapons, WEAPONS);

        ArrayList<IPlayer> players = new ArrayList<>();//players

        Scanner keyboard = new Scanner(System.in); // gets userInputs
        
        System.out.println("\nWelcome to Whodunit?  !!! \nStarting new Game.....");
        System.out.println("\nHow many opponents are desired?");

        int noOfOpponents = getNoOpponents(keyboard); // get the number of computer players from the user
        
        addPlayers(noOfOpponents, players); // creates players and add to the list
        
        Model game = new Model(players, suspects, locations, weapons); // create new game model.
        game.setUp();// set up the game.
        game.play();// play the game.

        keyboard.close();
    }


    //------------------------------------------------------
    // addCards
    //
    // PURPOSE:    creates card objects and add to the game
    // PARAMETERS:
    //              CardType:       type of card
    //              cards:          list to store the cards.
    //              names:          names of cards to be used in this game.
    // Returns: 
    //------------------------------------------------------
    private static void addCards(CardType cardType, ArrayList<Card> cards, String[] names) {
        for(String name:names){
            cards.add(new Card(cardType, name));
        }
    }


    //------------------------------------------------------
    // addPlayers
    //
    // PURPOSE:    add all the computer opponents to the game
    // PARAMETERS:
    //              compOpponents:      number of computer opponents
    //              players:            Arrarylist to add the players to.
    // Returns: 
    //------------------------------------------------------
    private static void addPlayers(int compOpponents, ArrayList<IPlayer> players){
        players.add(new Human());
        for(int i=0; i<compOpponents; i++){
            players.add(new Computer());
        }
    }


    //------------------------------------------------------
    // getNoOpponents
    //
    // PURPOSE:    get the number of computer opponents from human player
    // PARAMETERS:
    //              keyboard:      get iniput from user
    //
    // Returns:     The number of opponents
    //------------------------------------------------------
    private static int getNoOpponents(Scanner keyboard){
        boolean valid = false;
        int opponents = Integer.MIN_VALUE ;// number of computer opponents
        while(!valid){
            try{
                opponents = Integer.parseInt(keyboard.nextLine());
                valid = opponents > 0;
            }catch(Exception e){
                System.out.println("Enter a valid integer");
            }
            if(!valid && opponents != Integer.MIN_VALUE){
                System.out.println("Enter a number greater than 0");
            }
        }
        
        return opponents;
    }
}
