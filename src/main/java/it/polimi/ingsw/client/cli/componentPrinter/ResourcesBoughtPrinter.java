package it.polimi.ingsw.client.cli.componentPrinter;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;

/**
 * class that prints the resource array bought from the market or that has to be chosen in the initial part
 * of the game
 */
public class ResourcesBoughtPrinter {

    /**
     * method that prints the set of resources that a player can choose
     * @param resources is the array of resources that have to be printed
     * @param index is needed to differentiate the usage of this methos
     *              with index 0 also the tile is printed, otherwise only
     *              the set of resources is displayed
     */
    public static void print(ArrayList<Resource> resources, int index){

        if(index == 0){
            System.out.println(Constants.ANSI_BLUE + "\n" +
                    Constants.resourcesTitle +
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
