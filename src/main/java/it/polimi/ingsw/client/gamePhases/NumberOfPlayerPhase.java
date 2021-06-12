package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NumberOfPlayerMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.InputMismatchException;
import java.util.Scanner;

public class NumberOfPlayerPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        int number;
        Scanner scanner = new Scanner(System.in);
        System.out.print(Constants.ANSI_CYAN + "Enter number of players (1 to 4): " + Constants.ANSI_RESET);

        try{
            number = scanner.nextInt();
        }catch (InputMismatchException e){
            number = 6;
            scanner.nextLine();
        }

        cli.setNumberOfPlayers(number);

        while(cli.getNumberOfPlayers() < 1 || cli.getNumberOfPlayers() > 4){
            System.out.println(Constants.ANSI_RED + "Error! Invalid number of player" + Constants.ANSI_RESET);
            System.out.print(Constants.ANSI_CYAN + "Enter number of players (1 to 4): " + Constants.ANSI_RESET);
            try{
                number = scanner.nextInt();
            }catch (InputMismatchException e){
                number = 6;
                scanner.nextLine();
            }

            cli.setNumberOfPlayers(number);
        }

        cli.getClientSocket().send(new NumberOfPlayerMessage(String.valueOf(cli.getNumberOfPlayers())));
        cli.setGamePhase(new WaitingOtherPlayersPhase());
        new Thread(cli).start();
    }
}
