package it.polimi.ingsw.client.CLI.componentPrinter;

import it.polimi.ingsw.model.game.Resource;

import java.util.ArrayList;

public class ResourcesBoughtPrinter {
    public static void print(ArrayList<Resource> resources){
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
            System.out.print(i + "   ");
        }
        System.out.println("");
    }
}
