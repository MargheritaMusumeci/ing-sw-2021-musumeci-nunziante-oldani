package it.polimi.ingsw.client.cli.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class able to handle the action of activating a leader card
 */
public class ActiveLeaderCardPhase extends Phase {

    /**
     * method able to handle the action of activating a leader card
     * @param cli is client's cli
     */
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        //check if there is a leader card to be activated
        int possibleLeaderCards = 0;
        for (SerializableLeaderCard leaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if (!leaderCard.isActive()){
                possibleLeaderCards++;
            }
        }

        if(possibleLeaderCards == 0){
            System.out.println(Constants.ANSI_RED + "You have already activated all your cards!" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        ArrayList<SerializableLeaderCard> serializableLeaderCards = new ArrayList<>();
        for (SerializableLeaderCard leaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if (!leaderCard.isActive()){
                serializableLeaderCards.add(leaderCard);
            }
        }

        cli.printSetOfLeaderCard(serializableLeaderCards);

        boolean control;
        control = false;
        int number;
        do{
            System.out.print(Constants.ANSI_CYAN + "Choose the leader card to be activated (type the id): " + Constants.ANSI_RESET);
            try{
                number = scanner.nextInt();
            }catch (InputMismatchException e){
                number = 100;
                scanner.nextLine();
            }

            for(SerializableLeaderCard lCard : cli.getClientSocket().getView().getLeaderCards()){
                if(lCard.getId() == number && !lCard.isActive()){
                    control = true;
                    break;
                }
            }
            if(!control){
                System.out.println(Constants.ANSI_RED + "Error! There's no leader card with that id" + Constants.ANSI_RESET);
            }
        }while(!control);

        //find the position of the leader card in the set
        int pos = 0;
        for (int i=0; i<cli.getClientSocket().getView().getLeaderCards().size(); i++){
            if(cli.getClientSocket().getView().getLeaderCards().get(i).getId() == number){
                pos = i;
            }
        }

        //I'll have to send the message that the leader card was activated
        cli.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (cli.isAckArrived()){
            System.out.println(Constants.ANSI_GREEN + "Leader card correctly activated" + Constants.ANSI_RESET);
        }

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }
}
