package it.polimi.ingsw.model.game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.LeaderCard;

/**
 * Collection of LeaderCard
 */
public class LeaderCardSet {

    private List<LeaderCard> leaderCardSet;

    public LeaderCardSet(){

        leaderCardSet = new ArrayList<>();
        populateLeaderSet();
    }

    /**
     * Read cards from json and create them
     */
    private void populateLeaderSet(){

        String path = "C:/Users/margh/IdeaProjects/ing-sw-2021-musumeci-nunziante-oldani/src/main/resources/leaderCards.json"; //need to find the correct path
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            LeaderCard[] leaderCards = new Gson().fromJson(reader, LeaderCard[].class);
            LeaderCard leader;
            Collections.addAll(leaderCardSet, leaderCards);
            Collections.shuffle(leaderCardSet);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
