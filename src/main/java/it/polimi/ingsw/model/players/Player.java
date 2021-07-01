package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.io.Serializable;

public abstract class Player implements Serializable {

    protected String nickName;
    protected Dashboard dashboard;
    protected PopeTrack popeTrack;

    /**
     * Attribute that is false when the player is disconnected
     */
    protected boolean isPlaying;

    /**
     * Attribute set true in the end of the game if the player won
     */
    protected boolean isWinner;

    /**
     * @return the dashboard of the player
     */
    public Dashboard getDashboard(){ return dashboard; }

    /**
     * @return the nickName of the player
     */
    public String getNickName(){ return nickName; }

    /**
     * @return the popeTrack of the player
     */
    public PopeTrack getPopeTrack() {
        return popeTrack;
    }

    /**
     * Method that sets the player as the winner
     */
    public void setWinner(){
        this.isWinner = true;
    }

    /**
     * @return true if the player won
     */
    public boolean isWinner() {
        return isWinner;
    }

    /**
     * @return true if the player is connected, false otherwise
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Set if the player is playing or not
     * @param playing is a boolean and it is true if the player is playing
     */
    public void setPlaying(boolean playing) { isPlaying = playing; }

    public void setPopeTrack(PopeTrack popeTrack) {
        this.popeTrack = popeTrack;
    }

    public void setDashboard(Dashboard dashboard){
        this.dashboard = dashboard;
    }
}
