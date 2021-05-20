package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.UseLeaderCardMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UseLeaderCardPhase extends Phase {
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        //Check if the player has leader card
        if(cli.getClientSocket().getView().getLeaderCards().size() == 0){
            System.out.println(Constants.ANSI_RED + "You don't have leader card" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        //Check if there are active cards
        boolean check = false;
        for (SerializableLeaderCard serializableLeaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if(serializableLeaderCard.isActive()){
                check = true;
            }
        }

        if (!check){
            //Player doesn't have leader card
            System.out.println(Constants.ANSI_RED + "You don't have active card" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        //Check if there is a card not already used
        check = false;
        for (SerializableLeaderCard serializableLeaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if(serializableLeaderCard.isActive()){
                check = true;
            }
        }

        if (!check){
            //Player doesn't have leader card
            System.out.println(Constants.ANSI_RED + "You've already used all your leader card" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        //Print card the player can use
        ArrayList<SerializableLeaderCard> serializableLeaderCards = new ArrayList<>();
        for (SerializableLeaderCard serializableLeaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if(!serializableLeaderCard.isUsed() && serializableLeaderCard.isActive()){
               serializableLeaderCards.add(serializableLeaderCard);
            }
        }
        cli.printSetOfLeaderCard(serializableLeaderCards);

        boolean control = false;
        int number;
        do{
            System.out.print(Constants.ANSI_CYAN + "Choose the leader card to be activated (type the id): "+ Constants.ANSI_RESET);
            try{
                number = scanner.nextInt();
            }catch (InputMismatchException e){
                number = 100;
                scanner.nextLine();
            }

            for(SerializableLeaderCard lCard : cli.getClientSocket().getView().getLeaderCards()){
                if(lCard.getId() == number && !lCard.isUsed() && lCard.isActive()){
                    control = true;
                }
            }

            if(!control){
                System.out.println(Constants.ANSI_RED + "Error! There's no leader card with that id" + Constants.ANSI_RESET);
            }

        }while(!control);

        //Find the position of the card chose in the set
        int pos = 0;
        for (int i=0; i<cli.getClientSocket().getView().getLeaderCards().size(); i++){
            if(cli.getClientSocket().getView().getLeaderCards().get(i).getId() == number){
                pos = i;
            }
        }

        //Send the message and wait the answer
        cli.getClientSocket().send(new UseLeaderCardMessage("Use leader card", pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (cli.isAckArrived()){
            System.out.println(Constants.ANSI_GREEN + "Leader card correctly used" + Constants.ANSI_RESET);
        }

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }
}
