package it.polimi.ingsw.client.cli.componentPrinter;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLockBox;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;

/**
 * Class able to print the lock box
 */
public class LockBoxPrinter {

    /**
     * method that handles the creation and printing of the stock
     * @param serializableLockBox is the lock box that has to be printed
     */
    public static void print(SerializableLockBox serializableLockBox){

        String firstLine = "╔═══════════════════════╗";
        String lastLine = "╚═══════════════════════╝";
        String vertical = "║";

        System.out.println(Constants.ANSI_CYAN + "\n" +
                Constants.lockBoxTitle +
                Constants.ANSI_RESET);

        ArrayList<String>[] locBox = new ArrayList[3];
        for (int i =0; i<3; i++){
            locBox[i] = new ArrayList<>();
        }

        locBox[0].add(firstLine);
        locBox[1].add(vertical);
        locBox[1].add(" ");
        locBox[1].add(Constants.coin);
        locBox[1].add(" ");
        locBox[1].add(String.valueOf(serializableLockBox.getResources().get(Resource.COIN)));
        if(serializableLockBox.getResources().get(Resource.COIN) < 10){
            locBox[1].add(" ");
        }
        locBox[1].add(vertical);
        locBox[1].add(" ");
        locBox[1].add(Constants.servant);
        locBox[1].add(" ");
        locBox[1].add(String.valueOf(serializableLockBox.getResources().get(Resource.SERVANT)));
        if(serializableLockBox.getResources().get(Resource.SERVANT) < 10){
            locBox[1].add(" ");
        }
        locBox[1].add(vertical);
        locBox[1].add(" ");
        locBox[1].add(Constants.shield);
        locBox[1].add(" ");
        locBox[1].add(String.valueOf(serializableLockBox.getResources().get(Resource.SHIELD)));
        if(serializableLockBox.getResources().get(Resource.SHIELD) < 10){
            locBox[1].add(" ");
        }
        locBox[1].add(vertical);
        locBox[1].add(" ");
        locBox[1].add(Constants.rock);
        locBox[1].add(" ");
        locBox[1].add(String.valueOf(serializableLockBox.getResources().get(Resource.ROCK)));
        if(serializableLockBox.getResources().get(Resource.ROCK) < 10){
            locBox[1].add(" ");
        }

        locBox[1].add(vertical);

        locBox[2].add(lastLine);

        for (int i=0; i< locBox.length; i++){
            for (int j=0; j<locBox[i].size(); j++){
                if(j==locBox[i].size()-1){
                    System.out.println(locBox[i].get(j));
                }else{
                    System.out.print(locBox[i].get(j));
                }
            }
        }

    }
}
