import static org.junit.jupiter.api.Assertions.*;  //Junit 5
import org.junit.jupiter.api.Test;  // Junit 5
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;



// -----------------------------------
// AUTHOR            : Fisayo Olofin
// CLASS          : TestModel
// 
// 
// REMARKS: Perform test cases 
// -----------------------------------



public class TestModel {

    //variables used during the testcases

    //List to store cards
    private ArrayList<Card> suspects;
    private ArrayList<Card> locations;
    private ArrayList<Card> weapons;

    //To store a guess
    private Guess guess;

    //To store single cards
    private Card suspectCard;
    private Card locationCard;
    private Card weaponCard;
    
    private  final String[] SUSPECTS = {"Thanos", "Loki", "Kang"};
    private  final String[] LOCATIONS = {"Titan", "Asgard", "Knowhere"};
    private  final String[] WEAPONS = {"The Tesseract", "Loki's Scepter", "The Infinity Gaunlet"};

    private IPlayer compPlayer1;
    private IPlayer humanPlayer;

    @BeforeEach
    public void setUp(){
        
        suspects = new ArrayList<>();
        locations = new ArrayList<>();
        weapons = new ArrayList<>();

        //fill up the lists
        for(int i = 0 ; i < SUSPECTS.length ; i++)
        {
           suspects.add(new Card (CardType.SUSPECT ,SUSPECTS[i]));
           locations.add(new Card(CardType.LOCATION , LOCATIONS[i]));
           weapons.add(new Card(CardType.WEAPON , WEAPONS[i]));
        }

        //assign single cards
        suspectCard = suspects.get(0);
        locationCard = locations.get(0);
        weaponCard = weapons.get(0);

        guess = new Guess(suspectCard , locationCard , weaponCard , false);

        compPlayer1 = new Computer();
        humanPlayer  = new Human();

        compPlayer1.setUp(2 , 0 , suspects , locations , weapons);//player 1
        humanPlayer.setUp(2 , 1 , suspects , locations , weapons);//player 2
    }

    //If a computer player has no cards, then canAnswer should return null;
    @Test
    public void test1(){

        assertNull(compPlayer1.canAnswer(guess, humanPlayer),"should be null, compPlayer has no cards.");

    }//end Test1


    //If a computer player has exactly one card from a guess, canAnswer should return that card.
    @Test
    public void test2(){

        compPlayer1.setCard(suspectCard);// compPlayer1 has the Thanos suspect card

        assertEquals(suspectCard , compPlayer1.canAnswer(guess , humanPlayer), "canAnswer should return the only card player has.");
    }//end Test2

    @Test
    //If a computer player has more than one card from a guess, canAnswer should return one of the cards.
    public void test3()
    {   
        //CompPlayer1 is assigned 2 cards in the guess
        compPlayer1.setCard(suspectCard);
        compPlayer1.setCard(locationCard);

        Card answer = compPlayer1.canAnswer(guess , humanPlayer);

        if(answer.isEqual(suspectCard)){
            assertEquals(suspectCard , answer);
        } 
        else{
            assertEquals(locationCard , answer);
        }

    }//end Test3

    
    //If a computer player is given all but n cards (for some number n > 2 that you should choose) from the set of cards, ...
    //a call to getGuess should return a guess that does not contain any of the cards that the player has been given. ...
    //That is, an initial guess from a computer player must consist of cards it does not have.
    @Test
    public void test4()
    {   
        //give player all but 3 cards
        for(int i = 0 ; i < suspects.size() - 1 ; i++)
        {
           compPlayer1.setCard(suspects.get(i));
           compPlayer1.setCard(locations.get(i));
           compPlayer1.setCard(weapons.get(i));
        }

        guess = compPlayer1.getGuess();

        //The only cards player dont have. The last cards in the list, Therefore the initial guess should be the 3 cards
        suspectCard = suspects.get(suspects.size() - 1);
        locationCard = locations.get(locations.size() - 1);
        weaponCard = weapons.get(weapons.size() - 1);

        assertEquals(suspectCard,guess.getSuspect(),"both cards should be Kang");
        assertEquals(locationCard,guess.getLocation(),"both cards should be Knowhere");
        assertEquals(weaponCard,guess.getWeapon(),"both cards should be The Infinity Gaunlet");  

    }//end Test4

    @Test
    //If a computer player is given all but three cards from the set of cards, 
    //a call to getGuess should return the correct accusation (not a suggestion).
    public void test5()
    {
        //give player all but 3 cards
        for(int i = 0 ; i < suspects.size() - 1 ; i++)
        {
           compPlayer1.setCard(suspects.get(i));
           compPlayer1.setCard(locations.get(i));
           compPlayer1.setCard(weapons.get(i));
        }

        guess = compPlayer1.getGuess();

        //The only cards player dont have. The last cards in the list, Therefore the initial guess should be the 3 cards
        suspectCard = suspects.get(suspects.size() - 1);
        locationCard = locations.get(locations.size() - 1);
        weaponCard = weapons.get(weapons.size() - 1);

        assertTrue(guess.isAccusation(),"This should be an Accusation.");

    }//end Test5


    //If a computer player is given all but four cards from the set of cards, 
    //a call to getGuess should not return an accusation. However, 
    //if receiveInfo is called with one of the four cards, 
    //then after that, a second call to getGuess should return the correct accusation.
    @Test
    public void test6(){
        //give player all but 4 cards
        for(int i = 0 ; i < suspects.size() - 1 ; i++)
        {
           compPlayer1.setCard(suspects.get(i));
           compPlayer1.setCard(locations.get(i));
        }
        compPlayer1.setCard(weaponCard);//only given 1 of 3 weapon cards

        guess = compPlayer1.getGuess();
        assertFalse(guess.isAccusation(),"This should be an Suggestion.");

        //player receives info about the second weapons card
        compPlayer1.receiveInfo(humanPlayer, weapons.get(1));

        guess = compPlayer1.getGuess();
        assertTrue(guess.isAccusation(),"This should be an Accusation.");
    }//end Test6


    //If a human player is given some cards, and then canAnswer is called with a guess that includes 
    //one (or more) of the cards the player has, the method must return one of those cards 
    //(that is, the human player cannot give a card that they do not have in their hand – 
    //this will be achieved through input validation in your implementation).
    @Test
    public void test7(){
        
        //human player given 2 cards
        humanPlayer.setCard(suspectCard);
        humanPlayer.setCard(weaponCard);
        
        Card answer = humanPlayer.canAnswer(guess, compPlayer1);

        //Human response has to be one of the given cards.
        if(answer.isEqual(suspectCard)){
            assertEquals(suspectCard , answer);
        } 
        else{
            assertEquals(weaponCard , answer);
        }
    }//end Test7
    
}
