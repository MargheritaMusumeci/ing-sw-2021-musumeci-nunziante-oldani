package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.DiscardLeaderCardMessage;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

import java.util.Scanner;

public class DiscardLeaderCardPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);

        if(cli.getClientSocket().getView().getLeaderCards().size() == 0){
            System.out.println("Non hai leader card!");
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        //controllo che ci siano ancora carte non attivate
        boolean check = false;
        for (SerializableLeaderCard serializableLeaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if(!serializableLeaderCard.isActive()){
                check = true;
            }
        }
        if (!check){
            //non ho carte non attive
            System.out.println("Tutte le tue carte sono attivate, non puoi scartarle");
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }

        //stampo le carte scartabili
        for (SerializableLeaderCard serializableLeaderCard : cli.getClientSocket().getView().getLeaderCards()){
            if(!serializableLeaderCard.isActive()){
                cli.printSingleLeaderCard(serializableLeaderCard);
            }
        }

        boolean controllo = false;
        int number;
        do{
            System.out.println("Choose the leader card to be discard (type the id): ");
            number = scanner.nextInt();

            for(SerializableLeaderCard lCard : cli.getClientSocket().getView().getLeaderCards()){
                if(lCard.getId() == number && !lCard.isActive()){
                    controllo = true;
                }
            }
        }while(!controllo);

        //trovo la posizione a cui si trova la leader card nel mio set
        int pos = 0;
        for (int i=0; i<cli.getClientSocket().getView().getLeaderCards().size(); i++){
            if(cli.getClientSocket().getView().getLeaderCards().get(i).getId() == number){
                pos = i;
            }
        }

        //devo mandare il messsaggio di scarto carta
        cli.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (cli.isAckArrived()){
            System.out.println("Leader card correctly discarded");
        }else{
            System.out.println("Error while discarding the leader card");
        }

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }
}
