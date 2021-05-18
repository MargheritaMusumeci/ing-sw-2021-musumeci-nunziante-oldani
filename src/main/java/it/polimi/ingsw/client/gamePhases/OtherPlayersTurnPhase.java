package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;

public class OtherPlayersTurnPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {

        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
