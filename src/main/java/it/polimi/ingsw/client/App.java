package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        if(args.length == 0){
            //GUI.main(null);
            GUI gui = new GUI();
            gui.launch(args);
        }else{
            if(args[0].equals("cli")){
                new CLI();
            }else{
                GUI.main(null);
            }
        }

    }
}
