package it.polimi.ingsw.client.cli.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.componentPrinter.*;
import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.serializableModel.SerializableDashboard;
import it.polimi.ingsw.utils.Constants;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ShowOtherPlayersViewPhase extends Phase {

    @Override
    public void makeAction(CLI cli) {

        //print tile of the acriion
        System.out.println(Constants.ANSI_CYAN + "\n" +
                "   ____  _   _                 _____  _                           \n" +
                "  / __ \\| | | |               |  __ \\| |                          \n" +
                " | |  | | |_| |__   ___ _ __  | |__) | | __ _ _   _  ___ _ __ ___ \n" +
                " | |  | | __| '_ \\ / _ \\ '__| |  ___/| |/ _` | | | |/ _ \\ '__/ __|\n" +
                " | |__| | |_| | | |  __/ |    | |    | | (_| | |_| |  __/ |  \\__ \\\n" +
                " _\\____/ \\__|_| |_|\\___|_|    |_|    |_|\\__,_|\\__, |\\___|_|  |___/\n" +
                " \\ \\    / (_)                                  __/ |              \n" +
                "  \\ \\  / / _  _____      _____                |___/               \n" +
                "   \\ \\/ / | |/ _ \\ \\ /\\ / / __|                                   \n" +
                "    \\  /  | |  __/\\ V  V /\\__ \\                                   \n" +
                "     \\/   |_|\\___| \\_/\\_/ |___/                                   \n" +
                "                                                                  \n" +
                Constants.ANSI_RESET);

        Scanner scanner = new Scanner(System.in);
        String nickname;
        HashMap<Integer, String> idToNickname = new HashMap<>();
        int i=0;
        System.out.println(Constants.ANSI_GREEN + "You are currently playing with: "+ Constants.ANSI_RESET);
        for (String name : cli.getClientSocket().getView().getEnemiesDashboard().keySet()){
            System.out.println(Constants.ANSI_GREEN + i + ") " + name + Constants.ANSI_RESET);
            idToNickname.put(i, name);
            i++;
        }

        System.out.print(Constants.ANSI_CYAN + "Choose the player whose dashboard you want to see: " + Constants.ANSI_RESET);
        int number;
        do{
            try{
                number = scanner.nextInt();
            }catch (InputMismatchException e){
                number = 100;
                scanner.nextLine();
            }
        }while (number<0 || number>cli.getNumberOfPlayers() -1);

        String name = idToNickname.get(number);
        SerializableDashboard dashboard = cli.getClientSocket().getView().getEnemiesDashboard().get(name);

        StockPrinter.print(dashboard.getSerializableStock());
        LockBoxPrinter.print(dashboard.getSerializableLockBox());
        ProductionSectionPrinter.print(dashboard);
        LeaderCardsPrinter.print(cli.getClientSocket().getView().getEnemiesActivatedLeaderCards().get(dashboard));
        PopeTrackPrinter.print(dashboard.getSerializablePopeTack());
        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();

    }

}
