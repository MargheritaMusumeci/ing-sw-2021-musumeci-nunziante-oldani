package it.polimi.ingsw.client.gamePhases.myTurnPhases;


import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.CLI.componentPrinter.ResourcesBoughtPrinter;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.client.gamePhases.myTurnPhases.MyTurnPhase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.StoreResourcesMessage;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BuyFromMarketPhase extends Phase {
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        cli.printMarket();
        int scelta = 0;
        do {
            System.out.print(Constants.ANSI_CYAN + "Type 1 to pick a row or 2 for a column: " + Constants.ANSI_RESET);
            try{
                scelta = scanner.nextInt();
            }catch (InputMismatchException e){
                scelta = 6;
                scanner.nextLine();
            }


            if(scelta == 1){
                int row = 0;
                do{
                    System.out.print(Constants.ANSI_CYAN + "Insert the row that you want to buy (0,1,2): " + Constants.ANSI_RESET);
                    try{
                        row = scanner.nextInt();
                    }catch (InputMismatchException e){
                        row = 6;
                        scanner.nextLine();
                    }

                }while (row <0 || row > 2);

                cli.getClientSocket().send(new BuyFromMarketMessage("buy from market", row, true));

            }
            if(scelta == 2){
                int col = 0;
                do{
                    System.out.print(Constants.ANSI_CYAN + "Insert the columns that you want to buy (0,1,2,3): " + Constants.ANSI_RESET);
                    try{
                        col = scanner.nextInt();
                    }catch (InputMismatchException e){
                        col = 6;
                        scanner.nextLine();
                    }
                }while (col <0 || col > 3);

                cli.getClientSocket().send(new BuyFromMarketMessage("buy from market", col, false));
            }
        }while (scelta != 1 && scelta != 2);

        //System.out.println("Message sent");
        //Resources bought, waiting for ack/nack
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(cli.isAckArrived()){
            //System.out.println("Risosrse dal mercato prese in modo corretto");
            cli.setIsAckArrived(false);
            cli.setActionBeenDone(true);
        }else{
            System.out.println(Constants.ANSI_RED + "Error while buying the resources from market!" + Constants.ANSI_RESET);
            cli.setIsNackArrived(false);
        }

        //chiedo le risorse
        cli.getClientSocket().send(new RequestResourcesBoughtFromMarketMessage("Risorse che ho comprato richeiste"));

        synchronized (this){
            try {
                //System.out.println("Waiting for resources bought");
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
            System.out.println(Constants.ANSI_GREEN + "You have bought only white resources" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }


        do{
            //stampo le risorse ottenute
            ResourcesBoughtPrinter.print(cli.getClientSocket().getView().getResourcesBoughtFromMarker(), 0);
            System.out.println(Constants.ANSI_CYAN + "Choose the resources that you want to save in your stock. " +
                    "You can type 5 to save all the resources or -1 when you have finished." + Constants.ANSI_RESET);

            ArrayList<Integer> positions = new ArrayList<>();
            int positionSelected;

            do{
                System.out.print(Constants.ANSI_CYAN + "> ");
                try{
                    positionSelected = scanner.nextInt();
                }catch (InputMismatchException e){
                    positionSelected = 6;
                    scanner.nextLine();
                }
                if(positionSelected >= 0 &&
                        positionSelected < cli.getClientSocket().getView().getResourcesBoughtFromMarker().size() &&
                        !positions.contains(positionSelected)){
                    positions.add(positionSelected);
                }else{
                    if(positionSelected == 5){
                        positions = new ArrayList<>();
                        for (int j=0; j<cli.getClientSocket().getView().getResourcesBoughtFromMarker().size(); j++){
                            positions.add(j);
                        }
                        break;
                    }else{
                        if(positionSelected != -1)
                            System.out.println(Constants.ANSI_RED + "Error! The resources that you have chosen doesn't exist!" + Constants.ANSI_RESET);
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

        System.out.println(Constants.ANSI_GREEN + "Resources correctly saved in stock! \n\n" + Constants.ANSI_RESET);

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();

    }
}
