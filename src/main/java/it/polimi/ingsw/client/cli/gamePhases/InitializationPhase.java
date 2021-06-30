package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class able to handle the initialization phase in which the user have to insert
 * the ip address and port number of the server
 */
public class InitializationPhase extends Phase{

    /**
     * method able to handle the initialization phase in which the user have to insert
     *  * the ip address and port number of the server
     * @param cli is client's cli
     */
    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        int port;
        String address;

        cli.printTitle();

        System.out.print(Constants.ANSI_CYAN + "Enter the ip address of the Server: " + Constants.ANSI_RESET);
        address = scanner.next();

        System.out.print(Constants.ANSI_CYAN + "Enter the port: " + Constants.ANSI_RESET);

        try{
            port = scanner.nextInt();
        }catch (InputMismatchException e){
            port = 6;
            scanner.nextLine();
        }

        while(port < 1025 || port > 65535){
            System.out.println(Constants.ANSI_RED + "Invalid port number. Pick a port in range 1025-65535" + Constants.ANSI_RESET);
            System.out.print(Constants.ANSI_CYAN + "Enter the port: " + Constants.ANSI_RESET);
            try{
                port = scanner.nextInt();
            }catch (InputMismatchException e){
                port = 6;
                scanner.nextLine();
            }
        }

        try {
            cli.setSocket(new Socket(address, port));
            cli.setClientSocket(new ClientSocket(cli, cli.getSocket()));
        } catch (IOException e) {
            cli.setClientSocket(null);
            System.out.println(Constants.ANSI_RED + "There was a problem with the server. Please check if the ip address and port number" +
                    "are correct and if the server is up and running " + Constants.ANSI_RESET);
        }

        if(!(cli.getClientSocket() == null)){
            cli.setGamePhase(new NickNamePhase());
            new Thread(cli.getClientSocket()).start();
            new Thread(cli).start();
        }

    }
}
