package it.polimi.ingsw.client.cli.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.DiscardLeaderCardMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class able to handle the action of discarding a leader card
 */
public class DiscardLeaderCardPhase extends Phase{
    /**
     * method able to handle the action of discarding a leader card
     * @param cli is client cli
     */
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);

        if(cli.getClientSocket().getView().getLeaderCards().size() == 0){
            System.out.println(Constants.ANSI_RED + "There aren't leader cards available!" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        //i'll check that there are still unactivated cards
        boolean check = false;
        for (SerializableLeaderCard serializableLeaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if(!serializableLeaderCard.isActive()){
                check = true;
            }
        }
        if (!check){
            //all the cards are active
            System.out.println(Constants.ANSI_RED + "All the leader cards has already ben activate" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        //prints the possible leader cards
        ArrayList<SerializableLeaderCard> serializableLeaderCards = new ArrayList<>();
        for (SerializableLeaderCard leaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if (!leaderCard.isActive()){
                serializableLeaderCards.add(leaderCard);
            }
        }

        cli.printSetOfLeaderCard(serializableLeaderCards);

        boolean controllo = false;
        int number;
        do{
            System.out.print(Constants.ANSI_CYAN + "Choose the leader card to be discard (type the id): " + Constants.ANSI_RESET);
            try{
                number = scanner.nextInt();
            }catch (InputMismatchException e){
                number = 100;
                scanner.nextLine();
            }

            for(SerializableLeaderCard lCard : cli.getClientSocket().getView().getLeaderCards()){
                if(lCard.getId() == number && !lCard.isActive()){
                    controllo = true;
                }
            }

            if(!controllo){
                System.out.println(Constants.ANSI_RED + "Error! There's no leader card with that id" + Constants.ANSI_RESET);
            }

        }while(!controllo);

        //find the position in the set of the card
        int pos = 0;
        for (int i=0; i<cli.getClientSocket().getView().getLeaderCards().size(); i++){
            if(cli.getClientSocket().getView().getLeaderCards().get(i).getId() == number){
                pos = i;
            }
        }

        //send the message to the server with the position of the leader card
        cli.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (cli.isAckArrived()){
            System.out.println(Constants.ANSI_GREEN + "Leader card correctly discarded" + Constants.ANSI_RESET);
        }

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }
}
