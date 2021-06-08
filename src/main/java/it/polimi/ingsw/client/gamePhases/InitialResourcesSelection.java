package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.CLI.componentPrinter.ResourcesBoughtPrinter;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InitialResourcesSelection extends Phase{
    @Override
    public void makeAction(CLI cli) {

        int index;
        Scanner scanner = new Scanner(System.in);
        while(cli.getResources() == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(cli.getResources().size() == 4){
            ResourcesBoughtPrinter.print(cli.getResources(), 0);

            index = 0;
            do{
                System.out.print(Constants.ANSI_CYAN + "Choose the resource that you want start with (between 0 and 3): " + Constants.ANSI_RESET);
                try{
                    index = scanner.nextInt();
                }catch (InputMismatchException e){
                    index = 6;
                    scanner.nextLine();
                }
            }while (index < 0 || index > 3);

            ArrayList<Resource> selected = new ArrayList<Resource>();
            selected.add(cli.getResources().get(index));
            cli.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));
        }
        else if(cli.getResources().size() == 8){
            //creo due sottoArray
            ArrayList<Resource> first4 = (ArrayList<Resource>) cli.getResources().subList(0,4);
            ArrayList<Resource> last4 = (ArrayList<Resource>) cli.getResources().subList(4,8);

            //stampo i set
            ResourcesBoughtPrinter.print(first4, 0);
            ResourcesBoughtPrinter.print(last4, 4);

            index = 0;
            int index2 = 0;
            do{
                System.out.print(Constants.ANSI_CYAN + "Choose two resources that you want start with (between 0 and 7): " + Constants.ANSI_RESET);

                try{
                    index = scanner.nextInt();
                    index2 = scanner.nextInt();
                }catch (InputMismatchException e){
                    index = 16;
                    scanner.nextLine();
                }

            }while (index < 0 || index > 7 || index2 < 0 || index2 > 7 || index2 != index);

            ArrayList<Resource> selected = new ArrayList<Resource>();
            selected.add(cli.getResources().get(index));
            selected.add(cli.getResources().get(index2));
            cli.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));

        }else{
            cli.setGamePhase(new StartGamePhase());
            return;
        }

        try {
            System.out.println(Constants.ANSI_GREEN + "Wait for other players to choose their resources" + Constants.ANSI_RESET);
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalMonitorStateException e){
            e.printStackTrace();
        }

        if(cli.isAckArrived()){
            cli.setGamePhase(new StartGamePhase());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }else{
            cli.setIsNackArrived(false);
            System.err.println("Error while setting the initial resources, retry");
            new Thread(cli).start();
        }
    }
}
