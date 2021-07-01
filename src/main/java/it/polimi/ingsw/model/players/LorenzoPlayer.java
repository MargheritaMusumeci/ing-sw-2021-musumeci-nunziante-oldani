package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCardSet;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.io.Serializable;

public class LorenzoPlayer extends Player implements Serializable {

    private final LorenzoActionCardSet lorenzoActionCardSet;

    /**
     * LorenzoPlayer will receive the dashboard and the popeTrack of the player
     * @param popeTrack is the popeTrack of the player
     * @param dashboard is the dashboard of the player
     */
    public LorenzoPlayer(PopeTrack popeTrack , Dashboard dashboard, boolean persistence){
        this.nickName = "LorenzoIlMagnifico";
        this.popeTrack = popeTrack;
        if(!persistence){
            this.popeTrack.setLorenzoPosition();
        }

        this.dashboard = dashboard;
        this.isWinner = false;
        isPlaying = true;
        lorenzoActionCardSet = new LorenzoActionCardSet();
    }


    public LorenzoActionCardSet getLorenzoActionCardSet() {
        return lorenzoActionCardSet;
    }
}
