package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;

public class TurnHandler {

    private Game modelGame;

    /**
     * This attribute will be set true when one player previously ends the game
     *  and next active player would be the one with inkwell
     */
    private boolean isTheEnd;
    /**
     * This attribute will be set true when one player ends the game
     *  but the game must go on until the player with the inkwell
     */
    private boolean isTheLastTurn;

    /**
     * This attribute maintains the last popePosition that it's been reached by a player
     * We will use this attribute when one player reaches a pope position to control if he is the
     *      first one or if this section is already been discovered by an other player
     */
    private int lastSection;

    public TurnHandler(Game modelGame){
        this.modelGame=modelGame;
        lastSection=0;
        isTheLastTurn=false;
        isTheEnd=false;
    }

    /**
     * useless maybe better startGame?
     */
    public void startTurn(){

    }

    public void doAction(){

        //in base al messaggio che arriverà dal client chiamo il metodo corretto
        //posso controllare qua che l'utente non ha già effettuato l'azione
        //if(modelGame.getActivePlayer().getActionState()==false){}

        //se è un soloGame, ed è attivo Lorenzo allora pesco una carta LorenzoAction e chiamo il corrispondente metodo di DoActionLorenzo
    }

    /**
     * Check if the acrivePlayer has reached the end of the game
     */
    public void checkEndGame(){

        for (Player player :modelGame.getPlayers()) {
            if(player.getPopeTrack().getGamerPosition().getIndex()==25){
                isTheLastTurn=true;
                break;
            }
        }
        if(!isTheLastTurn && modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber()>6){
            isTheLastTurn=true;
        }

        Player followingPlayer = null;
        for(int i=0; i<modelGame.getPlayers().size(); i++){
            if(modelGame.getPlayers().get(i).equals(modelGame.getActivePlayer())){
                i++;
                if(i>= modelGame.getPlayers().size()){
                    i = 0;
                }
                followingPlayer = modelGame.getPlayers().get(i);
                break;
            }
        }

        if (followingPlayer != null && followingPlayer.getDashboard().getInkwell()) {
            isTheEnd = true;
        }
    }

    public void endGame(){

    }

    public int getLastSection() {
        return lastSection;
    }

    public void setLastSection(int lastSection) {
        this.lastSection = lastSection;
    }

    public boolean isTheEnd() {
        return isTheEnd;
    }

    public void setTheEnd(boolean theEnd) {
        isTheEnd = theEnd;
    }
}
