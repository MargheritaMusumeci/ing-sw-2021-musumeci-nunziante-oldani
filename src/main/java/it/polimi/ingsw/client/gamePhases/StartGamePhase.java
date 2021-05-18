package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.myTurnPhases.MyTurnPhase;

public class StartGamePhase extends Phase{


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
