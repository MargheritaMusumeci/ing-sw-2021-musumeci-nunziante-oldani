package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class DoActionLorenzo extends DoAction{


    public DoActionLorenzo(Game modelGame) {
        this.modelGame = modelGame;
    }

    @Override
    public void buyEvolutionCard(int row, int col) {
/*
        //cerco la prima carta ancora disponibile del colore che devo scartare

        while(modelGame.getEvolutionSection().getCard(row,col,0)==null){
            row--;
        }

        try {
            modelGame.getEvolutionSection().buy(row, col);
        } catch (ExcessOfPositionException e) {
            e.getLocalizedMessage();
        }
 */

    }

    @Override
    public void moveCross(int positions, ArrayList<Player> players) {

        //move only Lorenzo's Cross
        try {
            modelGame.getActivePlayer().getPopeTrack().updateLorenzoPosition(positions);
        } catch (ExcessOfPositionException e) {
            e.getLocalizedMessage();
        }
    }
}
