package it.polimi.ingsw.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class LeaderCardSet {

    private ArrayList<LeaderCard> leaderCardSet;

    public LeaderCardSet(){

        leaderCardSet = new ArrayList<LeaderCard>();
        populateLeaderSet();
    }

    private void populateLeaderSet(){

        String path = "C:/Users/margh/IdeaProjects/ing-sw-2021-musumeci-nunziante-oldani/src/main/resources/leaderCards.json"; //need to find the correct path
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            LeaderCard[] leaderCards = new Gson().fromJson(reader, LeaderCard[].class);
            LeaderCard leader;
            for (int i=0; i<leaderCards.length;i++) {
                leaderCardSet.add(leaderCards[i]);
            }
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
        return (ArrayList<LeaderCard>) leaderCardSet.clone();
    }

    /**
     *
     * @param position of the card to be returned
     * @return the card in the set at the given position
     */
    public LeaderCard getLeaderCard(int position) {
        //possibile esposizione del rep
        if ((position >= 0) && (position <= 16)) {
            return leaderCardSet.get(position);
        }
        return null;
    }

}
