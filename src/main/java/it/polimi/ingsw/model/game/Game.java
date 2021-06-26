package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    /**
     * Attribute that contains the players in the game
     */
    private final ArrayList<Player> players;

    private final Market market;
    private final EvolutionSection evolutionSection;
    private Player activePlayer;

    /**
     * Attribute that is true when every player in this game is disconnected
     */
    private boolean inPause;

    public Game(ArrayList<Player> players){
        this.players = players;
        this.market= new Market();
        this.evolutionSection = new EvolutionSection();
        this.activePlayer = players.get(0);

        assignLeaderCards();

        //Set the game in each player
        for(Player player : players){
            if(player instanceof HumanPlayer)
                ((HumanPlayer) player).setGame(this);
        }

        inPause = false;
    }

    /**
     * Method that sets 4 leader card for each player in the game, then the player during the configuration phase
     *      will choose 2 leader cards between the 4
     */
    private void assignLeaderCards() {

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        int count = 0;
        int position = 1;
        List<LeaderCard> lCards;
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                ArrayList<LeaderCard> playerSet = new ArrayList<>();
                lCards = leaderCardSet.getLeaderCardSet().subList(count, count+4);
                for(int i = 0; i < 4 ; i++){
                    playerSet.add(lCards.get(i));
                }
                ((HumanPlayer) player).setPosition(position);
                player.getDashboard().setLeaderCards(playerSet);
                count += 4;
                position++;
            }
        }
    }

    /**
     * Method that updates the current active player
     * @return the new active player , null if the game is in pause
     */
    public Player updateActivePlayer(){

        //Check if there is a at least an active player
        boolean checkPlayers = false;
        for (Player player:players){
            if (player.isPlaying()){
                //if the player is a lorenzo player it doesn't matter
                if(player instanceof HumanPlayer){
                    checkPlayers=true;
                }
            }
        }

        if (!checkPlayers){
            inPause = true;
            activePlayer = null;
            return null;
        }

        int j =0;
        for(int i=0; i<players.size(); i++){
           j++;
           if(activePlayer.equals(players.get(i))){
               if(j>= players.size()){
                   j = 0;
               }
               if(players.get(j).isPlaying()){
                   activePlayer = players.get(j);
                   return activePlayer;
               }else{
                   i--;
               }

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

    public boolean isInPause() {
        return inPause;
    }

    public void setInPause(boolean inPause) {
        this.inPause = inPause;
    }
}
