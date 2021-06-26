package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

/**
 * class that puts a player in wait until his turn has not been reached
 */
public class OtherPlayersTurnPhase extends Phase{

    /**
     * method that puts a player in wait until his turn has not been reached
     * @param cli
     */
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
