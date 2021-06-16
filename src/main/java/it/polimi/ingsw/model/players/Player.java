package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.osservables.PlayerObservable;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.io.Serializable;

public abstract class Player extends PlayerObservable implements Serializable {

    protected String nickName;
    protected Dashboard dashboard;
    protected PopeTrack popeTrack;
    protected boolean isPlaying;
    protected boolean isWinner;

    /**
     *
     * @return the dashboard of the player
     */
    public Dashboard getDashboard(){ return dashboard; }

    /**
     *
     * @return the nickName of the player
     */
    public String getNickName(){ return nickName; }

    /**
     *
     * @return the popeTrack of the player
     */
    public PopeTrack getPopeTrack() {
        return popeTrack;
    }

    /**
     * Method that set the player as the winner,in this way the view, in the end of the turn, can see herself who is/are
     *      the winner/winners
     *
     */
    public void setWinner(){
        this.isWinner = true;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        System.err.println("SWET IS PLATING UPDATEDDDDDDDDDDDDDDDD");
    }
}
