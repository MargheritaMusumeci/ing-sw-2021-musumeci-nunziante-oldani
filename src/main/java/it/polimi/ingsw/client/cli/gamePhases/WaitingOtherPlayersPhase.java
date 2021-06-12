package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

public class WaitingOtherPlayersPhase extends Phase{
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
