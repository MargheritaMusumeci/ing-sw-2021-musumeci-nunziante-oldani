package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

import java.util.Scanner;

public class ActiveLeaderCardPhase extends Phase {
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
            System.out.println("You have already activated all your cards!");
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        for (SerializableLeaderCard leaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if (!leaderCard.isActive()){
                cli.printSingleLeaderCard(leaderCard);
            }
        }

        boolean control;
        control = false;
        int number;
        do{
            System.out.println("Choose the leader card to be activated (type the id): ");
            number = scanner.nextInt();

            for(SerializableLeaderCard lCard : cli.getClientSocket().getView().getLeaderCards()){
                if(lCard.getId() == number && !lCard.isActive()){
                    control = true;
                }
            }
        }while(!control);

        //trovo la posizione a cui si trova la leader card nel mio set
        int pos = 0;
        for (int i=0; i<cli.getClientSocket().getView().getLeaderCards().size(); i++){
            if(cli.getClientSocket().getView().getLeaderCards().get(i).getId() == number){
                pos = i;
            }
        }

        //devo mandare il messsaggio di attivazione
        cli.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (cli.isAckArrived()){
            System.out.println("Leader card correctly activated");
        }else{
            System.out.println("Error while activating the leader card");
        }

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }
}
