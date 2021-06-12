package it.polimi.ingsw.client.cli.componentPrinter;

import it.polimi.ingsw.serializableModel.SerializablePopeTack;
import it.polimi.ingsw.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PopeTrackPrinter {

    public static void print(SerializablePopeTack serializablePopeTack){

        ArrayList<char[]> popeTrack = new ArrayList<>();
        boolean lorenzoInTheSamePosition = false;

        System.out.println(Constants.ANSI_BLUE + "\n" + Constants.popeTrackTitle + Constants.ANSI_RESET);

        try{
            Scanner s = new Scanner(Objects.requireNonNull(PopeTrackPrinter.class.getClassLoader().getResourceAsStream("popeTrack.txt")));
            String string;
            while (s.hasNextLine()){
                string = s.nextLine();
                char[] riga;
                riga = string.toCharArray();

                for (int i=0; i<riga.length;i++){
                    if(riga[i] <= 121 && riga[i] >= 97){
                        int x = (riga[i] - 97);
                        if(x == serializablePopeTack.getPosition()){
                            riga[i] = 'x';
                        }else{
                            riga[i] = ' ';
                        }

                        if(x == serializablePopeTack.getLorenzoPosition()){
                            if(serializablePopeTack.getPosition() == serializablePopeTack.getLorenzoPosition()){
                                lorenzoInTheSamePosition = true;
                            }else{
                                riga[i] = 'L';
                            }
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

        if(lorenzoInTheSamePosition){
            System.out.println(Constants.ANSI_GREEN + "Lorenzo is in your position!" + Constants.ANSI_RESET);
        }
    }
}
