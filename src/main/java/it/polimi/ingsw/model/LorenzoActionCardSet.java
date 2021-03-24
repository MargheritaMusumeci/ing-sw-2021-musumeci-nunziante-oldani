package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Manage the collection of 6 Solo Action tokens
 */
public class LorenzoActionCardSet {

    private ArrayList<LorenzoActionCard> actionSet;

    /**
     * random creation of the set
     */
    public LorenzoActionCardSet(){

        actionSet= new ArrayList<>();
        actionSet.add(0, new LorenzoActionCard(CardColor.blue));
        actionSet.add(1, new LorenzoActionCard(CardColor.yellow));
        actionSet.add(2, new LorenzoActionCard(CardColor.purple));
        actionSet.add(3, new LorenzoActionCard(CardColor.green));
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
        return firstCard;
    }

    /**
     * randomly shuffle of tokens
     */
    public void shuffle(){
        Collections.shuffle(actionSet);
    }
}
