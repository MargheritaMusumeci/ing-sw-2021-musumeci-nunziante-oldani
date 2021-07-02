package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.componentPrinter.ResourcesBoughtPrinter;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class that handles the phase in which players are requested to choose the initial resource
 * to start with
 */
public class InitialResourcesSelection extends Phase{

    /**
     * method that handles how many resources the player have to start with and allow the player to make his decision
     * @param cli is client's cli
     */
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

            do{
                System.out.print(Constants.ANSI_CYAN + "Choose the resource that you want start with (between 0 and 3): " + Constants.ANSI_RESET);
                try{
                    index = scanner.nextInt();
                }catch (InputMismatchException e){
                    index = 6;
                    scanner.nextLine();
                }
            }while (index < 0 || index > 3);

            ArrayList<Resource> selected = new ArrayList<>();
            selected.add(cli.getResources().get(index));
            cli.getClientSocket().send(new SelectedInitialResourceMessage("Resource chose" , selected));
        }
        else if(cli.getResources().size() == 8){
            ArrayList<Resource> first4 =  new ArrayList<>();
            ArrayList<Resource> last4 = new ArrayList<>();

            for (int i=0; i<4; i++){
                first4.add(cli.getResources().get(i));
                last4.add(cli.getResources().get(i+4));
            }


            ResourcesBoughtPrinter.print(first4, 0);
            ResourcesBoughtPrinter.print(last4, 4);


            int index2 = 0;
            do{
                System.out.print(Constants.ANSI_CYAN + "Choose two resources that you want start with (between 0 and 7): " + Constants.ANSI_RESET);

                try{
                    index = scanner.nextInt();
                    System.out.println(Constants.ANSI_CYAN + "Another one" + Constants.ANSI_RESET );
                    index2 = scanner.nextInt();
                }catch (InputMismatchException e){
                    index = 16;
                    scanner.nextLine();
                }

            }while (index < 0 || index > 7 || index2 < 0 || index2 > 7 || index == index2);

            ArrayList<Resource> selected = new ArrayList<>();
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
        }

        if(cli.isAckArrived()){
            cli.setGamePhase(new StartGamePhase());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }else{
            System.err.println("Error while setting the initial resources, retry");
            new Thread(cli).start();
        }
    }
}
