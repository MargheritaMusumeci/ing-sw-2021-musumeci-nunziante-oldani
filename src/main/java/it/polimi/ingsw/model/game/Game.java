package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private ArrayList<Player> players;
    private Market market;
    private EvolutionSection evolutionSection;
    private Player activePlayer;

    public Game(ArrayList<Player> players){
        this.players = players;
        this.market= Market.getInstanceOfMarket();
        this.evolutionSection = EvolutionSection.getInstanceOfEvolutionSection();
        this.activePlayer = players.get(0);

        assignLeaderCards();
    }

    private void assignLeaderCards() {

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        int count = 0;
        List<LeaderCard> lCards;
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                ArrayList<LeaderCard> playerSet = new ArrayList<LeaderCard>();
                lCards = leaderCardSet.getLeaderCardSet().subList(count, count+4);
                for(int i = 0; i < 4 ; i++)
                    playerSet.add(lCards.get(i));
                count += 4;
            }
        }
    }

    /**
     * method that updates the current active player
     * @return the new active player
     */
    public Player updateActivePlayer(){

       for(int i=0; i<players.size(); i++){
           if(activePlayer.equals(players.get(i))){
               i++;
               if(i>= players.size()){
                   i = 0;
               }
               activePlayer = players.get(i);
           }
        }

       return activePlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Market getMarket() {
        return market;
    }

    public EvolutionSection getEvolutionSection() {
        return evolutionSection;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

}
