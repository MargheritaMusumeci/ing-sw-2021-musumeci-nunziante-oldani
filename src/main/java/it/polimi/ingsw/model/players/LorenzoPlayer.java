package it.polimi.ingsw.model.players;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.util.ArrayList;

public class LorenzoPlayer extends Player {

    public LorenzoPlayer(String nickName , ArrayList<LeaderCard> leaderCards, boolean inkwell){
        this.nickName = nickName;
        this.popeTrack = new PopeTrack();
        this.isWinner = false;
        dashboard = new Dashboard(nickName , leaderCards , inkwell, popeTrack);
    }

}
