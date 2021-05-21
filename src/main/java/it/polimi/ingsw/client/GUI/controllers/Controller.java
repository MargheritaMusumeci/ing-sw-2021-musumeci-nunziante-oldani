package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;

/**
 * Class that contains common methods between all controllers.
 * 1) setGui is necessary because common information are stored only in GUI class
 * 2) init is necessary because with this method controllers could set the visibility of buttons, show errors and
 * prebuilt each scene.
 */
public interface Controller {

    void setGui(GUI gui);

    void init();
}
