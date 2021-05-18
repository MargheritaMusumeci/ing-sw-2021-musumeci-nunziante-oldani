package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;
import java.util.Scanner;

public class ActiveProductionZonePhase extends Phase {
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);

        int position = 0;
        boolean activeBasic = false;
        boolean exit = false;
        ArrayList<Integer> productionZones =new ArrayList<Integer>();
        ArrayList<Resource> resourcesRequires = new ArrayList<Resource>();
        ArrayList<Resource> resourcesEnsures = new ArrayList<Resource>();
        int numPZ = cli.getClientSocket().getView().getDashboard().getSerializableProductionZones().length +
                cli.getClientSocket().getView().getDashboard().getSerializableLeaderProductionZones().length;

        cli.printProductionZones();

        System.out.println("Which production zone do you want to activate? \n" +
                "You can choose between 0 , 1 , 2 and 3 , 4 (if you have an active leader production zone)\n" +
                "Insert -1 to end");
        do{
            //Insert the position
            do{
                position = scanner.nextInt();
                if(position < -1 || position > numPZ)
                    System.out.println("Position not valid, insert an other position");
            }while(position < -1 || position > numPZ);

            //If the player ended his choice
            if(position == -1){
                exit = true;
                break;
            }

            //Add the production if the position is valid
            if(!productionZones.contains(position))
                productionZones.add(position);
            else
                System.out.println("Position already chose");

        }while(!exit && productionZones.size() <= numPZ);

        //Now the array with the position is ready

        System.out.println("Do you want to activate the basic production zone? Y/N");

        do{
            exit = true;
            String input = scanner.next();
            if(input.equals("Y")){
                activeBasic = true;
            }
            else if(input.equals("N")){
                activeBasic = false;
            }
            else{
                System.out.println("Wrong parameter, try again");
                exit = false;
            }
        }while(!exit);

        //If the player wants to active the basic production
        if(activeBasic){
            System.out.println("Choose 2 resource types to use in basic production:");
            System.out.println("1) COIN");
            System.out.println("2) ROCK");
            System.out.println("3) SHIELD");
            System.out.println("4) SERVANT");

            while(resourcesRequires.size() < 2){
                fillArrayList(resourcesRequires);
            }

            System.out.println("Choose 1 resource type to obtain from basic production:");
            System.out.println("1) COIN");
            System.out.println("2) ROCK");
            System.out.println("3) SHIELD");
            System.out.println("4) SERVANT");

            while(resourcesEnsures.size() < 1){
                fillArrayList(resourcesEnsures);
            }
        }

        //Send activeProductionMessage to the server
        cli.getClientSocket().send(new ActiveProductionMessage("Active production zones" , productionZones ,
                activeBasic , resourcesRequires , resourcesEnsures));

        //Wait for a response
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (cli.isAckArrived()){
            cli.setActionBeenDone(true);
            System.out.println("Production zones activated");
        }else{
            System.out.println("Something goes wrong, try again");
        }

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }

    private void fillArrayList(ArrayList<Resource> resources){
        Scanner scanner = new Scanner(System.in);
        int position = 0;
        position = scanner.nextInt();
        if(position < 1 || position > 4){
            System.out.println("Wrong resources , try again");
            return;
        }
        if(position == 1)
            resources.add(Resource.COIN);
        else if(position == 2)
            resources.add(Resource.ROCK);
        else if(position == 3)
            resources.add(Resource.SHIELD);
        else
            resources.add(Resource.SERVANT);
    }
}
