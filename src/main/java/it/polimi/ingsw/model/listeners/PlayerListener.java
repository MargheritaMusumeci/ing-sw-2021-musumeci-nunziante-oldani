package it.polimi.ingsw.model.listeners;

import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

public interface PlayerListener {

    void update(ArrayList<Resource> resources);
}
