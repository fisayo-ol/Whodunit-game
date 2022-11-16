// -----------------------------------
// AUTHOR            : Fisayo Olofin
// CLASS          : Guess
// 
// 
// REMARKS: represents a players guess 
// -----------------------------------

public class Guess{
    //Instance Variables
    private Card weapon;
    private Card suspect;
    private Card location;

    boolean accusation;

    //Constructor
    public Guess(Card suspectGuess,Card locationGuess, Card weaponGuess, boolean isAccusation){
        weapon = weaponGuess;
        suspect = suspectGuess;
        location = locationGuess;

        accusation = isAccusation;
    }

    //getters
    public Card getWeapon(){
        return weapon;
    }

    public Card getSuspect(){
        return suspect;
    }

    public Card getLocation(){
        return location;
    }

    public boolean isAccusation(){
        return accusation;
    }
    
    public String toString(){
        return suspect + " with " + weapon + " on "+ location;
    }
}
