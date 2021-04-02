package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardSet {

    private ArrayList<LeaderCard> leaderCardSet;

    public LeaderCardSet(){
        leaderCardSet = new ArrayList<LeaderCard>();
        //build the deck with the gson library
        //instanzia le 16 carte leader e fa lo shuffle tanto lo fa una volta sola
    }

    /**
     *
     * @return the entire set
     */
    public ArrayList<LeaderCard> getLeaderCardSet() {
        return (ArrayList<LeaderCard>) leaderCardSet.clone();
    }

    /**
     *
     * @param position of the card to be returned
     * @return the card in the set at the given position
     */
    public LeaderCard getLeaderCard(int position){
        //possibile esposizione del rep
        return leaderCardSet.get(position);
    }

}
