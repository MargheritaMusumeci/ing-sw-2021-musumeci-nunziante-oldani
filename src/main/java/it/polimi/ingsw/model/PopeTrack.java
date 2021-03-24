package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PopeTrack {
    private Position[] track;//25
    private PopeCard[] popeCard;//3
    private Position gamerPosition;
    private Position lorenzoPosition;//su UML è int

    public PopeTrack(PopeCard popeCard0 , PopeCard popeCard1 , PopeCard popeCard2){
        track = new Position[25];//setto qua le 25 posizioni o ricevo un array di posizioni già create?
        popeCard = new PopeCard[3];
        popeCard[0] = new PopeCard(popeCard0.getPoint() , popeCard0.getPosition());
        popeCard[1] = new PopeCard(popeCard1.getPoint() , popeCard1.getPosition());
        popeCard[2] = new PopeCard(popeCard2.getPoint() , popeCard2.getPosition());
        gamerPosition = track[0];
        lorenzoPosition = track[0];
    }

    public Position getGamerPosition(){   //ci sono 2 getPosition nell' UML
        return gamerPosition;
    }

    public void updateGamerPosition(int increment){
        gamerPosition = track[gamerPosition.getIndex() + increment];
    }

    public PopeCard[] getPopeCard() { return popeCard.clone(); }

    public void setGamerPosition(Position position){ }//Is it really necessary????

    public Position getLorenzoPosition(){ return lorenzoPosition; }

    public void updateLorenzoPosition(int increment){
        lorenzoPosition = track[lorenzoPosition.getIndex() + increment];
    }
}
