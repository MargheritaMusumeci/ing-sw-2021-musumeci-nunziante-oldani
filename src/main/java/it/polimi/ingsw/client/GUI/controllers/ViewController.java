package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;


public class ViewController implements Controller{
    private GUI gui;

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }
}
