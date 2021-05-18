package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NumberOfPlayerMessage;

import java.util.Scanner;

public class NumberOfPlayerPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of players (1 to 4): ");
        cli.setNumberOfPlayers(scanner.nextInt());

        while(cli.getNumberOfPlayers() < 1 || cli.getNumberOfPlayers() > 4){
            System.out.println("Error! Invalid number of player");
            System.out.print("Enter number of players (1 to 4): ");
            cli.setNumberOfPlayers(scanner.nextInt());
        }

        cli.getClientSocket().send(new NumberOfPlayerMessage(String.valueOf(cli.getNumberOfPlayers())));
        cli.setGamePhase(new WaitingOtherPlayersPhase());
        new Thread(cli).start();
    }
}
