package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

public abstract class Player {

    protected String nickName;
    protected Dashboard dashboard;
    protected PopeTrack popeTrack;

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
}
