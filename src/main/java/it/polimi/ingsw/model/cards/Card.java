package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.game.Resource;

import java.util.HashMap;

public interface Card {
     int point = 0;
     HashMap<Resource, Integer> requires = null;
     HashMap<Resource, Integer> products = null;
     boolean isActive = false;

    public int getPoint();
    public boolean isActive();
    public void setActive(boolean value);
    public HashMap<Resource, Integer> getRequires();
    public HashMap<Resource, Integer> getProduction();


}
