package it.polimi.ingsw.client.CLI.componentPrinter;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;

public class ResourcesBoughtPrinter {
    public static void print(ArrayList<Resource> resources, int index){

        if(index == 0){
            System.out.println(Constants.ANSI_BLUE + "\n" +
                    "  _____                                         \n" +
                    " |  __ \\                                        \n" +
                    " | |__) |___  ___  ___  _   _ _ __ ___ ___  ___ \n" +
                    " |  _  // _ \\/ __|/ _ \\| | | | '__/ __/ _ \\/ __|\n" +
                    " | | \\ \\  __/\\__ \\ (_) | |_| | | | (_|  __/\\__ \\\n" +
                    " |_|  \\_\\___||___/\\___/ \\__,_|_|  \\___\\___||___/\n" +
                    "                                                \n" +
                    Constants.ANSI_RESET);
        }


        int count = 0;
        String upArrow = "\u2191";
        System.out.print(" ┃ ");
        for (Resource resource: resources){
            System.out.print(resource.label + " ┃ ");
            count++;
        }
        System.out.println("");
        System.out.print("   ");
        for (int i=0; i<count; i++){
            System.out.print(upArrow + "   ");
        }
        System.out.println("");
        System.out.print("   ");
        for (int i=0; i<count; i++){
            System.out.print(i + index + "   ");
        }
        System.out.println("");
    }
}
