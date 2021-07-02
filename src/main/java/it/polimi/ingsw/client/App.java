package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;

/**
 * Class able to start the correct type of client (cli or gui) based
 * on arguments. The default comportment achieved with no arguments is the
 * start of the client in gui mode
 */
public class App {
    /**
     * main method of client
     * @param args contains the string "cli" or "gui"
     */
    public static void main( String[] args ) {

        if(args.length == 0){
            GUI.main(null);
        }else{
            if(args[0].equals("cli")){
                new CLI();
            }else{
                GUI.main(null);
            }
        }

    }
}
