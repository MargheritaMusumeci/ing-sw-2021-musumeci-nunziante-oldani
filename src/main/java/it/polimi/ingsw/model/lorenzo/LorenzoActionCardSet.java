package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.cards.CardColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Manage the collection of 6 Solo Action tokens
 */
public class LorenzoActionCardSet implements Serializable {

    private final ArrayList<LorenzoActionCard> actionSet;

    /**
     * random creation of the set
     */
    public LorenzoActionCardSet(){

        actionSet= new ArrayList<>();
        actionSet.add(0, new LorenzoActionCard(CardColor.BLUE,2));
        actionSet.add(1, new LorenzoActionCard(CardColor.YELLOW,2));
        actionSet.add(2, new LorenzoActionCard(CardColor.PURPLE,2));
        actionSet.add(3, new LorenzoActionCard(CardColor.GREEN,2));
        actionSet.add(4, new LorenzoActionCard(1));
        actionSet.add(5, new LorenzoActionCard(2));
        Collections.shuffle(actionSet);
    }

    /**
     * returns first Solo Action token and shift this one to the last position of the list
     * @return is the current Solo Action token
     */
    public LorenzoActionCard getActionCard(){
        LorenzoActionCard firstCard;
        firstCard = actionSet.remove(0); //returns the object removed
        actionSet.add(5,firstCard);
        return firstCard; //restituisce il riferimento :( ma non implementiamo il setter per modificarlo
    }

    /**
     * randomly shuffle of tokens
     */
    public void shuffle(){
        Collections.shuffle(actionSet);
    }

}
