package it.polimi.ingsw.client.CLI.componentPrinter;

import it.polimi.ingsw.serializableModel.SerializablePopeTack;
import it.polimi.ingsw.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PopeTrackPrinter {

    public static void print(SerializablePopeTack serializablePopeTack){

        File file = new File("src/main/resources/popeTrack.txt");
        ArrayList<char[]> popeTrack = new ArrayList<>();

        System.out.println(file);

        System.out.println(Constants.ANSI_BLUE + "\n" +
                "  _____                   _______             _    \n" +
                " |  __ \\                 |__   __|           | |   \n" +
                " | |__) |__  _ __   ___     | |_ __ __ _  ___| | __\n" +
                " |  ___/ _ \\| '_ \\ / _ \\    | | '__/ _` |/ __| |/ /\n" +
                " | |  | (_) | |_) |  __/    | | | | (_| | (__|   < \n" +
                " |_|   \\___/| .__/ \\___|    |_|_|  \\__,_|\\___|_|\\_\\\n" +
                "            | |                                    \n" +
               Constants.ANSI_RESET);

        try{
            Scanner s = new Scanner(file);
            String string;
            while (s.hasNextLine()){
                string = s.nextLine();
                char[] riga = new char[string.length()];
                riga = string.toCharArray();

                for (int i=0; i<riga.length;i++){
                    if(riga[i] <= 121 && riga[i] >= 97){
                        int x = (riga[i] - 97);
                        if(x == serializablePopeTack.getPosition()){
                            riga[i] = 'x';
                        }else{
                            riga[i] = ' ';
                        }

                    }
                    if(riga[i] == 'A'){
                        if(serializablePopeTack.getActiveCards()[0]){
                            riga[i] = 'A';
                        }else{
                            if(serializablePopeTack.getDiscardCards()[0]){
                                riga[i] = 'D';
                            }else{
                                riga[i] = ' ';
                            }
                        }


                    }
                    if(riga[i] == 'B'){
                        if(serializablePopeTack.getActiveCards()[1]){
                            riga[i] = 'A';
                        }else{
                            if(serializablePopeTack.getDiscardCards()[1]){
                                riga[i] = 'D';
                            }else{
                                riga[i] = ' ';
                            }
                        }


                    }
                    if(riga[i] == 'C'){
                        if(serializablePopeTack.getActiveCards()[2]){
                            riga[i] = 'A';
                        }else{
                            if(serializablePopeTack.getDiscardCards()[2]){
                                riga[i] = 'D';
                            }else{
                                riga[i] = ' ';
                            }
                        }
                    }
                }

                popeTrack.add(riga);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        for (char[] r : popeTrack){
            for (char c : r){
                System.out.print(c);
            }
            System.out.println();
        }
    }
}
