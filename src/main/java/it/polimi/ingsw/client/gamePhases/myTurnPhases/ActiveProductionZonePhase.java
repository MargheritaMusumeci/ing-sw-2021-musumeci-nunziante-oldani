package it.polimi.ingsw.client.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.componentPrinter.ResourcesBoughtPrinter;
import it.polimi.ingsw.client.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
        ArrayList<Resource> leaderProduction = new ArrayList<>();

        int numPZ = cli.getClientSocket().getView().getDashboard().getSerializableProductionZones().length +
                cli.getClientSocket().getView().getDashboard().getSerializableLeaderProductionZones().length;

        cli.printProductionZones();

        System.out.println(Constants.ANSI_CYAN + "Insert which production zone do you want to activate \n" +
                "You can choose between 0 , 1 , 2 and 3 , 4 (if you have an active leader production zone)\n" +
                "Insert -1 to end" + Constants.ANSI_RESET);
        do{
            //Insert the position
            do{
                try{
                    position = scanner.nextInt();

                }catch (InputMismatchException e){
                    position = 6;
                    scanner.nextLine();
                }
                if(position < -1 || position > numPZ)
                    System.out.println(Constants.ANSI_RED + "Error! Position not valid, insert another position" + Constants.ANSI_RESET);
            }while(position < -1 || position > numPZ);

            //If the player ended his choice
            if(position == -1){
                exit = true;
                break;
            }

            //Add the production if the position is valid
            if(!productionZones.contains(position)){
                productionZones.add(position);
                if(position == 3 || position == 4){
                    //chiedo la risorsa
                    System.out.println(Constants.ANSI_CYAN + "Choose which resource will be produced: " + Constants.ANSI_RESET);
                    System.out.println("1) "+Resource.COIN.label+"\t2) "+Resource.ROCK.label+"\t3) "+Resource.SERVANT.label+"\t4) "+Resource.SHIELD.label);
                    System.out.print(Constants.ANSI_CYAN + "> " + Constants.ANSI_RESET);
                    int resourceChoice;
                    do{
                        try{
                            resourceChoice = scanner.nextInt();
                        }catch (InputMismatchException e){
                            resourceChoice = 6;
                            System.out.println(Constants.ANSI_RED + "Error! Resources not found" + Constants.ANSI_RESET);
                            System.out.print(Constants.ANSI_CYAN + "> " + Constants.ANSI_RESET);
                        }
                    }while (resourceChoice<1 || resourceChoice>4);
                    //aggiungo le risorse alla lista da mandare
                    switch (resourceChoice){
                        case 1:
                            leaderProduction.add(Resource.COIN);
                            break;
                        case 2:
                            leaderProduction.add(Resource.ROCK);
                            break;
                        case 3:
                            leaderProduction.add(Resource.SERVANT);
                            break;
                        case 4:
                            leaderProduction.add(Resource.SHIELD);
                            break;
                    }
                }
            }
            else
                System.out.println(Constants.ANSI_RED + "Position already chose" + Constants.ANSI_RESET);

        }while(!exit && productionZones.size() <= numPZ);

        //Now the array with the position is ready

        System.out.println(Constants.ANSI_CYAN + "Do you want to activate the basic production zone? Y/N" + Constants.ANSI_RESET);

        do{
            exit = true;
            String input = scanner.next();
            if(input.equals("Y") || input.equals("y")){
                activeBasic = true;
            }
            else if(input.equals("N") || input.equals("n")){
                activeBasic = false;
            }
            else{
                System.out.println(Constants.ANSI_RED + "Wrong parameter, try again" + Constants.ANSI_RESET);
                exit = false;
            }
        }while(!exit);

        //If the player wants to active the basic production
        if(activeBasic){
            ArrayList<Resource> resources = new ArrayList<>();
            resources.add(Resource.COIN);
            resources.add(Resource.ROCK);
            resources.add(Resource.SHIELD);
            resources.add(Resource.SERVANT);
            ResourcesBoughtPrinter.print(resources, 0);
            System.out.println(Constants.ANSI_CYAN + "Choose 2 resource types to use in basic production:" + Constants.ANSI_RESET);


            while(resourcesRequires.size() < 2){
                fillArrayList(resourcesRequires);
            }

            ResourcesBoughtPrinter.print(resources, 0);
            System.out.print(Constants.ANSI_CYAN + "Choose 1 resource type to obtain from basic production: "+Constants.ANSI_RESET);

            while(resourcesEnsures.size() < 1){
                fillArrayList(resourcesEnsures);
            }
        }

        //Send activeProductionMessage to the server
        cli.getClientSocket().send(new ActiveProductionMessage("Active production zones" , productionZones ,
                activeBasic , resourcesRequires , resourcesEnsures, leaderProduction));

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
            System.out.println(Constants.ANSI_GREEN + "Production zones activated" + Constants.ANSI_RESET);
        }

        cli.setIsAckArrived(false);
        cli.setIsNackArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }

    private void fillArrayList(ArrayList<Resource> resources){
        Scanner scanner = new Scanner(System.in);
        int position = 0;

        try{
            position = scanner.nextInt();
        }catch (InputMismatchException e){
            position = 6;
            scanner.nextLine();
        }

        if(position < 0 || position > 3){
            System.out.println(Constants.ANSI_RED + "Wrong resources, try again" + Constants.ANSI_RESET);
            return;
        }
        if(position == 0)
            resources.add(Resource.COIN);
        else if(position == 1)
            resources.add(Resource.ROCK);
        else if(position == 2)
            resources.add(Resource.SHIELD);
        else
            resources.add(Resource.SERVANT);
    }
}
