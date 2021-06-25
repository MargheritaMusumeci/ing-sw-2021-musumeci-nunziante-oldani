package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

/**
 * class that puts a player in wait until the number of player requested have joined the game
 */
public class WaitingOtherPlayersPhase extends Phase{

    /**
     * method that notify the player that he has to wait until other players join the game
     * and then puts the player in wait
     * @param cli is client's cli
     */
    @Override
    public void makeAction(CLI cli) {
        System.out.println(Constants.ANSI_GREEN + "Wait for the other players to join the game!" + Constants.ANSI_RESET);
        try {
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
