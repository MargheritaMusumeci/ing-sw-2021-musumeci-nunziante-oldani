package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.gamePhases.myTurnPhases.MyTurnPhase;

/**
 * class that handles the start of the game
 */
public class StartGamePhase extends Phase{

    /**
     * method that handles the start of the game and puts the game phase in "my turn phase"
     * @param cli is client's cli
     */
    @Override
    public void makeAction(CLI cli) {
        System.out.println("The game is started");
        if(cli.getNumberOfPlayers() == 1){
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
        }else{
            try {
                synchronized (this){
                    wait();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
