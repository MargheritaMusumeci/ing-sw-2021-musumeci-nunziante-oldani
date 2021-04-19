package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Game;

public class TurnHandler {

    private Game modelGame;

    public TurnHandler(Game modelGame){
        this.modelGame=modelGame;
    }

    /**
     * useless
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

        if((modelGame.getActivePlayer().getDashboard().getPopeTrack().getGamerPosition().getIndex()==25)||
                (modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber()>6)){
            endGame();
        }
    }

    public void endGame(){
    }
}
