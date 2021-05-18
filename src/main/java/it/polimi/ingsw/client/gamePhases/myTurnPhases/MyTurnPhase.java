package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;

import java.util.Scanner;

public class MyTurnPhase extends Phase {
    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("It's your turn");

        cli.printMenu();

        int action = scanner.nextInt();

        switch (action) {
            case 0:
                if (!cli.isActionBeenDone()) {
                    System.out.println("Cannot end turn without do an action");
                    new Thread(cli).start();
                    return;
                } else {
                    cli.getClientSocket().send(new EndTurnMessage("Turn ended"));
                }
                break;
            case 1:
                cli.printLeaderCards();
                new Thread(cli).start();
                break;
            case 2:
                cli.printStock();
                new Thread(cli).start();
                break;
            case 3:
                cli.printLockBox();
                new Thread(cli).start();
                break;
            case 4:
                cli.printPopeTrack();
                new Thread(cli).start();
                break;
            case 5:
                cli.printProductionZones();
                new Thread(cli).start();
                break;
            case 6:
                cli.printMarket();
                new Thread(cli).start();
                break;
            case 7:
                cli.printEvolutionSection();
                new Thread(cli).start();
                break;


            case 8:
                cli.setGamePhase(new ActiveLeaderCardPhase());
                new Thread(cli).start();
                break;
            case 9:
                cli.setGamePhase(new DiscardLeaderCardPhase());
                new Thread(cli).start();
                break;
            case 10:
                if (!cli.isActionBeenDone()) {
                    cli.setGamePhase(new BuyFromMarketPhase());
                } else {
                    System.out.println("You have already make an action, yuo should end your turn now!");
                }
                new Thread(cli).start();
                break;
            case 11:
                if (!cli.isActionBeenDone()) {
                    cli.setGamePhase(new ActiveProductionZonePhase());
                } else {
                    System.out.println("You have already make an action, yuo should end your turn now!");
                }
                new Thread(cli).start();
                break;
            case 12:
                if (!cli.isActionBeenDone()) {
                    cli.setGamePhase(new BuyEvolutiuonCardPhase());
                } else {
                    System.out.println("You have already make an action, yuo should end your turn now!");

                }
                new Thread(cli).start();
                break;

            case 13:
                cli.setGamePhase(new UseLeaderCardPhase());
                new Thread(cli).start();;
                break;

            default:
                System.out.println("This action doesn't exist");
                new Thread(cli).start();
                break;
        }
    }
}
