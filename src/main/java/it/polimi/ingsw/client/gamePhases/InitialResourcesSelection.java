package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;
import java.util.Scanner;

public class InitialResourcesSelection extends Phase{
    @Override
    public void makeAction(CLI cli) {
        int index;
        Scanner scanner = new Scanner(System.in);
        while(cli.getResources() == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(cli.getResources().size() == 4){
            for(int i = 0 ; i < 4 ; i++){
                System.out.println("Risorsa " + i + ": " + cli.getResources().get(i));
            }

            index = 0;
            do{
                System.out.println("Scegli una risorsa(tra 0 e 3)");
                index = scanner.nextInt();
            }while (index < 0 || index > 3);

            ArrayList<Resource> selected = new ArrayList<Resource>();
            selected.add(cli.getResources().get(index));
            cli.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));
        }
        else if(cli.getResources().size() == 8){
            for(int i = 0 ; i < 8 ; i++){
                System.out.println("Risorsa " + i + ": " + cli.getResources().get(i));
            }

            index = 0;
            int index2 = 0;
            do{
                System.out.println("Scegli 2 risorse(tra 0 e 7)");
                index = scanner.nextInt();
                index2 = scanner.nextInt();

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
        }
    }
}
