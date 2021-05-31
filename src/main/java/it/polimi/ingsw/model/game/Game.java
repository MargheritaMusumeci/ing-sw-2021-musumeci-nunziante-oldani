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

    private void assignLeaderCards() {

        LeaderCardSet leaderCardSet = new LeaderCardSet();
        int count = 0;
        int position = 1;
        List<LeaderCard> lCards;
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                ArrayList<LeaderCard> playerSet = new ArrayList<LeaderCard>();
                lCards = leaderCardSet.getLeaderCardSet().subList(count, count+4);
                for(int i = 0; i < 4 ; i++){
                    playerSet.add(lCards.get(i));
                    //System.out.println("Card " + i + ": " + lCards.get(i).getRequiresForActiveLeaderCards() + " , " + lCards.get(i).getAbilityType() + "\n");
                }
                ((HumanPlayer) player).setPosition(position);
                player.getDashboard().setLeaderCards(playerSet);
                count += 4;
                position++;
            }
        }
    }

    /**
     * method that updates the current active player
     * @return the new active player
     */
    public Player updateActivePlayer(){

        //check if there is a at least an active player
        boolean checkPlayers = false;
        for (Player player:players){
            if (player.isPlaying()){
                //if the player is a lorenzo player it doesn't matter
                if(player instanceof HumanPlayer){
                    checkPlayers=true;
                    System.err.println("ho trovato un plater attivo");
                }
            }
        }

        if (!checkPlayers){
            inPause = true;
            activePlayer = null;
            System.err.println("non ci sono player attivi");
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
                   System.err.println("ho aggiornato il player: i="+i+" j= "+j);
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
