package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;

public class WaitingOtherPlayersPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        try {
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
