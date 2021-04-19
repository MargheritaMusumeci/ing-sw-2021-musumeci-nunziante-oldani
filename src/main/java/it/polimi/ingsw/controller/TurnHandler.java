package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

public class TurnHandler {

    private Game modelGame;

    public TurnHandler(Game modelGame){
        this.modelGame=modelGame; //ha senso metterlo sia quì che nel DoActionPlayer? Potrei passare lo stock da aggiornare, il popeTrack da incrementare ecc...
    }

    /**
     * useless
     */
    public void startTurn(){

    }

    public void doAction(){
        //in base al messaggio che arriverà dal client chiamo il metodo corretto
        //posso controllare qua che l'utente non ha già effettuato l'azione

    }

    /**
     * check if there is a winner
     */
    public void checkEndGame(){
        if((modelGame.getActivePlayer().getDashboard().getPopeTrack().getGamerPosition().getIndex()==25)||
                (modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber()>6)){
            endGame();
        }
    }

    public void endGame(){

    }
}
