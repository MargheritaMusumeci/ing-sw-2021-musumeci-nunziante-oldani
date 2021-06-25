package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.Constants;

/**
 * class able to handle the final phase of the game when someone has won
 *
 */
public class EndGamePhase extends Phase{

    /**
     * method able to end the visualization of the final points gained by the players.
     * It is also responsible to show the winner
     * @param cli is client's cli
     */
    @Override
    public void makeAction(CLI cli) {
        System.out.println(Constants.ANSI_RED + "\n" +
                "  _______ _             _____                      \n" +
                " |__   __| |           / ____|                     \n" +
                "    | |  | |__   ___  | |  __  __ _ _ __ ___   ___ \n" +
                "    | |  | '_ \\ / _ \\ | | |_ |/ _` | '_ ` _ \\ / _ \\\n" +
                "    | |  | | | |  __/ | |__| | (_| | | | | | |  __/\n" +
                "  _ |_|  |_|_|_|\\___|  \\_____|\\__,_|_| |_| |_|\\___|\n" +
                " (_)      / __ \\                                   \n" +
                "  _ ___  | |  | |_   _____ _ __                    \n" +
                " | / __| | |  | \\ \\ / / _ \\ '__|                   \n" +
                " | \\__ \\ | |__| |\\ V /  __/ |                      \n" +
                " |_|___/  \\____/  \\_/ \\___|_|                      \n" +
                "                                                   \n" +
                Constants.ANSI_RESET);


        boolean checkWinner = false;
        for (String name : cli.getClientSocket().getView().getWinners()){
            if(name.equals(cli.getNickname())){
                System.out.println(Constants.ANSI_GREEN + "You Won" + Constants.ANSI_RESET);
                checkWinner = true;
            }
        }

        if(!checkWinner){
            System.out.println(Constants.ANSI_RED + "You Lose" + Constants.ANSI_RESET);
        }

        for (String s : cli.getClientSocket().getView().getScores().keySet()){
            System.out.println(Constants.ANSI_YELLOW + s + ": " + cli.getClientSocket().getView().getScores().get(s));
        }

    }
}
