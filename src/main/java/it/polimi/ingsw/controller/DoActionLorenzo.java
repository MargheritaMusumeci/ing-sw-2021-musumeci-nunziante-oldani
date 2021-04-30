package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.lorenzo.LorenzoActionCard;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;

public class DoActionLorenzo extends DoAction{

    LorenzoActionCard lorenzoActionCard;

    public DoActionLorenzo(Game modelGame) {
        this.modelGame = modelGame;
    }

    public LorenzoActionCard getLorenzoActionCard() {
        return lorenzoActionCard;
    }

    public void setLorenzoActionCard(LorenzoActionCard lorenzoActionCard) {
        this.lorenzoActionCard = lorenzoActionCard;
    }

    @Override
    public void buyEvolutionCard(int row, int col) {

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
