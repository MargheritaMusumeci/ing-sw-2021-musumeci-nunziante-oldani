package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCardSet;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.util.ArrayList;

public class LorenzoPlayer extends Player {

    private LorenzoActionCardSet lorenzoActionCardSet;

    /**
     * LorenzoPlayer will receive the dashboard and the popeTrack of the player
     * @param popeTrack is the popeTrack of the player
     * @param dashboard is the dashboard of the player
     */
    public LorenzoPlayer(PopeTrack popeTrack , Dashboard dashboard){
        this.nickName = "Lorenzo";
        this.popeTrack = popeTrack;
        this.dashboard = dashboard;
        this.isWinner = false;
        lorenzoActionCardSet = new LorenzoActionCardSet();
    }

    public LorenzoActionCardSet getLorenzoActionCardSet() {
        return lorenzoActionCardSet;
    }
}
