package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

public class OtherPlayersTurnPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {

        synchronized (this){
            System.out.println(Constants.ANSI_YELLOW + "It's not your turn!" +Constants.ANSI_RESET);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
