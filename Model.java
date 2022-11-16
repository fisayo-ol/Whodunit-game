import java.util.ArrayList;
import java.util.Collections;

// -----------------------------------
// AUTHOR            : Fisayo Olofin
// STUDENT NUMBER  : 7812316
// CLASS          : Model
// 
// 
// REMARKS: Organize Cards and players in game
// -----------------------------------

public class Model{
    //Instance Variables
    private ArrayList<IPlayer> players;
    private ArrayList<Card> suspects;
    private ArrayList<Card> locations;
    private ArrayList<Card> weapons;

    private ArrayList<ArrayList<Card>> cardLists; //an 2D list of a clone of all the cards
    
    private ArrayList<Card> solution; // a list of the game solution arranged in order [suspect, location, weapon]
    private ArrayList<Card> allPlayingCards; // a list of all cards excluding the solution cards

    //Constructor
    public Model(ArrayList<IPlayer> players, ArrayList<Card> suspects, ArrayList<Card> locations, ArrayList<Card> weapons){
        this.players = players;
        this.suspects = suspects;
        this.locations = locations;
        this.weapons = weapons;

        cardLists = new ArrayList<>();
        cardLists.add(new ArrayList<>(suspects));
        cardLists.add(new ArrayList<>(locations));
        cardLists.add(new ArrayList<>(weapons));

        solution = new ArrayList<>();
        allPlayingCards = new ArrayList<>();
    }


    //------------------------------------------------------
    // setUp
    //
    // PURPOSE:    sets up the game, distribute the cards, seat the players, get the solution 
    //             and display info to the human player
    // PARAMETERS:
    //             
    // Returns: 
    //------------------------------------------------------
    public void setUp(){
        displayInfo();// print the list of all the cards to the user.

        loadPlayers();// shuffle and seat players at the table

        getSolution(); // shuffle the card and get the solution

        distributeCards();// distribute cards to players
    }// end setUp

    public void play(){
        ArrayList<IPlayer> currentPlayers = new ArrayList<>(players);// list of all active players
        boolean gameOver = false; //boolean flag to track when game is over

        int index = 0;
        IPlayer activePlayer = currentPlayers.get(index); //first player
        Guess currentGuess = null;

        while (!gameOver){
            System.out.println("Current Turn: Player " + activePlayer.getIndex());

            currentGuess = activePlayer.getGuess();//get the player's guess

            if(currentGuess.isAccusation()){
                gameOver = handleAccusation(currentPlayers, currentGuess, players.indexOf(activePlayer) );
            }
            else{
                handleSuggustion(activePlayer, currentGuess);
            }

            if(!gameOver){
                index = (currentPlayers.indexOf(activePlayer) + 1) % currentPlayers.size();
                activePlayer = currentPlayers.get(index);
            }
        }

        if(currentPlayers.size() == 1){
            System.out.println("All other players were eliminated therefore, Player " + currentPlayers.get(0).getIndex() + " wins.");
        }else{
            System.out.println("Player " + activePlayer.getIndex() + " won the game with '" + currentGuess + "'");
        }
        System.out.println("\nThe End !!!!!!");
    }// end play


    //------------------------------------------------------
    // displayInfo
    //
    // PURPOSE:    display info to the human player
    // PARAMETERS:
    //             
    // Returns: 
    //------------------------------------------------------
    private void displayInfo(){   
        System.out.println("\nShowing all the information:");
        for(ArrayList<Card> list: cardLists){
            System.out.println("Here are all the " + list.get(0).getType() + "s: ");
            System.out.println(list);
        }
    }//end displayInfo


    //------------------------------------------------------
    // loadPlayers
    //
    // PURPOSE:    seat players at the table
    // PARAMETERS:
    //             
    // Returns: 
    //------------------------------------------------------
    private void loadPlayers(){
        int index = 0;
        Collections.shuffle(players);
        for(IPlayer player : players){
            player.setUp(players.size(), index++, suspects, locations, weapons);
        }
    }//end loadPlayers


    //------------------------------------------------------
    // getSolution
    //
    // PURPOSE:    get a solution combination to the game
    // PARAMETERS:
    //             
    // Returns: 
    //------------------------------------------------------
    private void getSolution(){
        for(ArrayList<Card> list: cardLists){
            Collections.shuffle(list); //shuffle the cards
            int randPos = (int)(Math.random() * list.size());// select a random position
            solution.add(list.remove(randPos)); //get a random card
        }
    }//end getSolution


    //------------------------------------------------------
    // distributeCards
    //
    // PURPOSE:    distribute remaining cards to the players in the game
    // PARAMETERS:
    //             
    // Returns: 
    //------------------------------------------------------
    private void distributeCards(){
        //add all the remaining cards except the solution to the playing cards list
        for(ArrayList<Card> list: cardLists){
            allPlayingCards.addAll(list);
        }

        Collections.shuffle(allPlayingCards);//shuffle the cards

        //Assign cards to the players
        int index = 0;
        for(Card card:allPlayingCards){
            players.get(index).setCard(card);
            index = (index+1) % players.size();
        }
    }//end distributeCards

    //------------------------------------------------------
    // handleAccusation
    //
    // PURPOSE:    helper method to handle when an accusation is made
    // PARAMETERS:
    //              activePlayer:       player that made the accusation
    //              currentGuess:       guess made by the player
    //              currIndex:          index of currentPlayer
    //              currentPlayers:     players in the game
    //
    // Returns: true or false based on validity of the guess
    //------------------------------------------------------
    private boolean handleAccusation(ArrayList<IPlayer> currentPlayers, Guess currentGuess, int currIndex){
        System.out.println( "Player " + currIndex +  " made an Accusation: " + currentGuess);
        boolean gameOver = isValidAccusation(currentGuess);
        if(!gameOver){
            System.out.println("Player " + currIndex +  " made a bad accusation and was removed from the game.");
            currentPlayers.remove(players.get(currIndex));
            gameOver = currentPlayers.size() == 1;
        }
        return gameOver;
    }//end handleAccusation

    private boolean isValidAccusation(Guess currentGuess){
        boolean isValid = currentGuess.getSuspect().isEqual(solution.get(0)) 
                        && currentGuess.getLocation().isEqual(solution.get(1))
                        && currentGuess.getWeapon().isEqual(solution.get(2));
        return isValid;
    }//end isValidAccusation


    //------------------------------------------------------
    // handleSuggustion
    //
    // PURPOSE:    helper method to handle when a suggestion is made
    // PARAMETERS:
    //              activePlayer:       player that made the suggestion
    //              currentGuess:       guess made by the player
    // Returns: 
    //------------------------------------------------------
    private void handleSuggustion( IPlayer activePlayer, Guess currentGuess){
        System.out.println("Player " + activePlayer.getIndex() + " made a Suggestion: " + currentGuess);
        boolean answered = false;
        boolean isValid = true;
        IPlayer nextPlayer = null;
        IPlayer answeredBy = null;
        Card cardShown = null;
        int index = (activePlayer.getIndex()+1) % players.size();// start from the next player

        while(!answered && isValid){
            System.out.println("Asking player " + index + ".");
            nextPlayer = players.get(index);
            cardShown = nextPlayer.canAnswer(currentGuess, activePlayer);
            answered = cardShown != null;
            
            if(answered){
                answeredBy = players.get(index);
            }

            index = (index+1) % players.size();
            isValid = index != activePlayer.getIndex();
        }

        if(!answered){
            System.out.println("No one could answer.");
        }
        else{
            System.out.println("Player " + answeredBy.getIndex() + " answered.");
        }

        activePlayer.receiveInfo(answeredBy, cardShown);
    }//end handleSuggestion


 
}