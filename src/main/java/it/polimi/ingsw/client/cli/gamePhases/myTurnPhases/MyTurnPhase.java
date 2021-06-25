package it.polimi.ingsw.client.cli.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.cli.CLI;

import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class that coordinates the clients actions during his turn
 */
public class MyTurnPhase extends Phase {
    /**
     * method able to handle the input and choose from the clint's different action that he can do
     * during the turn
     * @param cli is client cli
     */
    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        System.out.println(Constants.ANSI_YELLOW + "It's your turn" + Constants.ANSI_RESET);

        cli.printMenu();

        int action;
        try {
            action = scanner.nextInt();
        } catch (InputMismatchException e) {
            action = 100;
            scanner.nextLine();
        }

        switch (action) {
            case 0:
                if (!cli.isActionBeenDone()) {
                    System.out.println(Constants.ANSI_RED + "Cannot end turn without do an action" + Constants.ANSI_RESET);
                    new Thread(cli).start();
                    break;
                } else {
                    cli.setActionBeenDone(false);
                    cli.getClientSocket().send(new EndTurnMessage("Turn ended"));
                }
                break;
            case 7:
                cli.printLeaderCards();
                new Thread(cli).start();
                break;
            case 8:
                cli.printStock();
                new Thread(cli).start();
                break;
            case 9:
                cli.printLockBox();
                new Thread(cli).start();
                break;
            case 10:
                cli.printPopeTrack();
                new Thread(cli).start();
                break;
            case 11:
                cli.printProductionZones();
                new Thread(cli).start();
                break;
            case 12:
                cli.printMarket();
                new Thread(cli).start();
                break;
            case 13:
                cli.printEvolutionSection();
                new Thread(cli).start();
                break;


            case 1:
                cli.setGamePhase(new ActiveLeaderCardPhase());
                new Thread(cli).start();
                break;
            case 2:
                cli.setGamePhase(new DiscardLeaderCardPhase());
                new Thread(cli).start();
                break;
            case 3:
                if (!cli.isActionBeenDone()) {
                    cli.setGamePhase(new BuyFromMarketPhase());
                } else {
                    System.out.println(Constants.ANSI_RED + "You have already make an action, yuo should end your turn now!" + Constants.ANSI_RESET);
                }
                new Thread(cli).start();
                break;
            case 4:
                if (!cli.isActionBeenDone()) {
                    cli.setGamePhase(new ActiveProductionZonePhase());
                } else {
                    System.out.println(Constants.ANSI_RED + "You have already make an action, yuo should end your turn now!" + Constants.ANSI_RESET);
                }
                new Thread(cli).start();
                break;
            case 5:
                if (!cli.isActionBeenDone()) {
                    cli.setGamePhase(new BuyEvolutiuonCardPhase());
                } else {
                    System.out.println(Constants.ANSI_RED + "You have already make an action, yuo should end your turn now!" + Constants.ANSI_RESET);
                }
                new Thread(cli).start();
                break;


            case 6:
                if (cli.getNumberOfPlayers() == 1) {
                    System.out.println(Constants.ANSI_RED + "You are the only player!" + Constants.ANSI_RESET);
                    new Thread(cli).start();
                    break;
                }
                cli.setGamePhase(new ShowOtherPlayersViewPhase());
                new Thread(cli).start();
                break;
            default:
                System.out.println(Constants.ANSI_RED + "This action doesn't exist" + Constants.ANSI_RESET);
                new Thread(cli).start();
                break;
        }
    }
}
