package it.polimi.ingsw.model.game;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.LeaderCard;

/**
 * Collection of LeaderCard
 */
public class LeaderCardSet implements Serializable{

    private List<LeaderCard> leaderCardSet;

    public LeaderCardSet(){

        leaderCardSet = new ArrayList<>();
        populateLeaderSet();
    }

    /**
     * Read cards from json and create them
     */
    private void populateLeaderSet(){
        LeaderCard[] leaderCards = new Gson().fromJson(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("json/leaderCards.json")), LeaderCard[].class);
        Collections.addAll(leaderCardSet, leaderCards);
        Collections.shuffle(leaderCardSet);
    }

    /**
     *
     * @return the entire set
     */
    public ArrayList<LeaderCard> getLeaderCardSet() {
        return (ArrayList<LeaderCard>) leaderCardSet;
    }

    /**
     *
     * @param position of the card to be returned
     * @return the card in the set at the given position
     */
    public LeaderCard getLeaderCard(int position) {

        if ((position >= 0) && (position <= 16)) {
            return leaderCardSet.get(position);
        }
        return null;
    }

}
