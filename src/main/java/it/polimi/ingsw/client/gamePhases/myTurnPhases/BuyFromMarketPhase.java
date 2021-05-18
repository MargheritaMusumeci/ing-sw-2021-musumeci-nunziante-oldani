package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.client.gamePhases.myTurnPhases.MyTurnPhase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.StoreResourcesMessage;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;
import java.util.Scanner;

public class BuyFromMarketPhase extends Phase {
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        cli.printMarket();
        int scelta = 0;
        do {
            System.out.println("Inserire 1 per scegliere una riga o 2 per scelgiere una colonna:");
            System.out.print(">");

            scelta = scanner.nextInt();

            if(scelta == 1){
                int row = 0;
                do{
                    System.out.println("Inserire la riga che si vuole acquistare: (0,1,2)");
                    row = scanner.nextInt();
                }while (row <0 || row > 2);

                cli.getClientSocket().send(new BuyFromMarketMessage("buy from market", row, true));

            }
            if(scelta == 2){
                int col = 0;
                do{
                    System.out.println("Inserire la colonna che si vuole acquistare: (0,1,2,3)");
                    col = scanner.nextInt();
                }while (col <0 || col > 3);

                cli.getClientSocket().send(new BuyFromMarketMessage("buy from market", col, false));
            }
        }while (scelta != 1 && scelta != 2);

        System.out.println("Message sent");
        //Resources bought, waiting for ack/nack
        synchronized (this){
            try {
                wait();
                System.out.println("I've been woke up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(cli.isAckArrived()){
            System.out.println("Risosrse dal mercato prese in modo corretto");
            cli.setIsAckArrived(false);
            cli.setActionBeenDone(true);
        }else{
            System.out.println("Errore durante l'acquisto delle risorse");
            cli.setIsNackArrived(false);
        }

        //chiedo le risorse
        cli.getClientSocket().send(new RequestResourcesBoughtFromMarketMessage("Risorse che ho comprato richeiste"));

        synchronized (this){
            try {
                System.out.println("Waiting for resources bought");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //controllo se tra le risorse ottunute ho solo nothing, in quel caso mando un messaggio e termino
        int countNotnull = 0;
        for (Resource resource: cli.getClientSocket().getView().getResourcesBoughtFromMarker()){
            if(resource != Resource.NOTHING){
                countNotnull++;
            }
        }

        //erano tutto nulle
        if(countNotnull == 0){
            cli.getClientSocket().send(new StoreResourcesMessage("salva risorse", cli.getClientSocket().getView().getResourcesBoughtFromMarker()));
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }


        do{
            //stampo le risorse ottenute
            int i =0;
            System.out.println("seleziona le risorse da inserire nello stock, -1 per terminare e 5 per sceglierle tutte");
            for (Resource resource: cli.getClientSocket().getView().getResourcesBoughtFromMarker()){
                System.out.println(i + ") "+resource);
                i++;
            }
            ArrayList<Integer> positions = new ArrayList<>();
            int positionSelected;

            do{
                positionSelected = scanner.nextInt();
                if(positionSelected >= 0 &&
                        positionSelected < cli.getClientSocket().getView().getResourcesBoughtFromMarker().size() &&
                        !positions.contains(positionSelected)){
                    System.out.println("prova if");
                    positions.add(positionSelected);
                }else{
                    if(positionSelected == 5){
                        positions = new ArrayList<>();
                        for (int j=0; j<cli.getClientSocket().getView().getResourcesBoughtFromMarker().size(); j++){
                            positions.add(j);
                        }
                        break;
                    }
                }
            }while(positionSelected != -1);

            ArrayList<Resource> resourcesToSend = new ArrayList<>();
            for(Integer integer: positions){
                resourcesToSend.add(cli.getClientSocket().getView().getResourcesBoughtFromMarker().get(integer));
            }
            cli.getClientSocket().send(new StoreResourcesMessage("salva risorse selezionate", resourcesToSend));

            synchronized (this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while(!cli.isAckArrived());

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();

    }
}
