// -----------------------------------
// AUTHOR            : Fisayo Olofin
// STUDENT NUMBER  : 7812316
// CLASS          : Card
// 
// 
// REMARKS: Represent a card in the game
// -----------------------------------

public class Card {
    //Instance Variable
    private CardType type;
    private String value;

    //Constructor
    public Card(CardType cardType, String cardValue){
        type = cardType;
        value = cardValue;
    }

    //getters
    public String getType(){
        String output = "" + type;
        return output.toLowerCase();
    }

    public String getValue(){
        return value;
    }

    public String toString(){
        return value;
    }
    
    public boolean isEqual(Card other){
        return type.equals(other.type) && value.equals(other.value);
    }
}
