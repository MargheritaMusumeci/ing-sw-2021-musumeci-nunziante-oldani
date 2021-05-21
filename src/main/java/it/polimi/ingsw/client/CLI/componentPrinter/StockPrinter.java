package it.polimi.ingsw.client.CLI.componentPrinter;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableStock;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;

public class StockPrinter {

    public static void print(SerializableStock serializableStock){
        ArrayList<Resource[]>  boxesPlus = serializableStock.getBoxPlus();
        if(boxesPlus == null){
            boxesPlus = new ArrayList<>();
        }
        ArrayList<Resource[]> boxes = serializableStock.getBoxes();

        System.out.println(Constants.ANSI_GREEN + "\n" +
                "   _____ _             _    \n" +
                "  / ____| |           | |   \n" +
                " | (___ | |_ ___   ___| | __\n" +
                "  \\___ \\| __/ _ \\ / __| |/ /\n" +
                "  ____) | || (_) | (__|   < \n" +
                " |_____/ \\__\\___/ \\___|_|\\_\\\n" +
                "                            \n" +
                Constants.ANSI_RESET);

        ArrayList<String>[] stock = new ArrayList[8];
        for (int i=0; i<8; i++){
            stock[i] = new ArrayList<>();
        }

        stock[0].add("NORMAL:");
        if(boxesPlus.size()!= 0){
            stock[0].add("     PLUS:");
        }

        stock[1].add(" ");

        stock[2].add("  ");
        if(boxes.get(0)[0] != null){
            stock[2].add(boxes.get(0)[0].label);
        }else{
            stock[2].add("#");
        }
        stock[2].add("   ");


        if(boxesPlus.size()!= 0){
            stock[2].add("     ");
            if (boxesPlus.get(0)[0] != null){
                stock[2].add(boxesPlus.get(0)[0].label +" ");
            }else{
                stock[2].add("# ");
            }

            if (boxesPlus.get(0)[1] != null){
                stock[2].add(boxesPlus.get(0)[1].label);
            }else{
                stock[2].add("#");
            }

        }

        stock[3].add(" ");

        stock[4].add(" ");
        if(boxes.get(1)[0] != null){
            stock[4].add(boxes.get(1)[0].label);
        }else{
            stock[4].add("#");
        }

        stock[4].add(" ");

        if(boxes.get(1)[1] != null){
            stock[4].add(boxes.get(1)[1].label);
        }else{
            stock[4].add("#");
        }

        stock[4].add(" ");

        if(boxesPlus.size() > 1){
            stock[4].add("     ");
            if (boxesPlus.get(1)[0] != null){
                stock[4].add(boxesPlus.get(1)[0].label +" ");
            }else{
                stock[4].add("# ");
            }

            if (boxesPlus.get(1)[1] != null){
                stock[4].add(boxesPlus.get(1)[1].label);
            }else{
                stock[4].add("#");
            }

        }

        stock[5].add(" ");

        if(boxes.get(2)[0] != null){
            stock[6].add(boxes.get(2)[0].label);
        }else{
            stock[6].add("#");
        }

        stock[6].add(" ");

        if(boxes.get(2)[1] != null){
            stock[6].add(boxes.get(2)[1].label);
        }else{
            stock[6].add("#");
        }

        stock[6].add(" ");

        if(boxes.get(2)[2] != null){
            stock[6].add(boxes.get(2)[2].label);
        }else{
            stock[6].add("#");
        }


        stock[7].add(" ");

        for (int i=0; i< stock.length; i++){
            for (int j=0; j<stock[i].size(); j++){
                if(j==stock[i].size()-1){
                    System.out.println(stock[i].get(j));
                }else{
                    System.out.print(stock[i].get(j));
                }
            }
        }



    }
}
