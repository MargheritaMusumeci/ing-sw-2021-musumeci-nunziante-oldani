package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;

/**
 * Class that takes care of showing the waiting scene necessary when a player has to wait for the others to configure
 * the game (choice of leader cards and resources).
 */
public class WaitingController implements Controller{

    private GUI gui;
    
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void init() {}
}
