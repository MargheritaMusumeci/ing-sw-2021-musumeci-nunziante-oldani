package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InitializationPhase extends Phase{
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
            System.out.println(Constants.ANSI_RED + "Invalid port number. Pick a porta in range 1025-65535" + Constants.ANSI_RESET);
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
            System.out.println(Constants.ANSI_RED + "There was a problem with the server. Please chek if the ip address and port number" +
                    "are correct and if the server is up and running " + Constants.ANSI_RESET);
            //probabilemte qui c'è un errore con il server (o con i dati inseriti ip/port)
        }

        if(cli.getClientSocket() == null){
            cli.setServerIsUp(false);
        }else{
            cli.setGamePhase(new NickNamePhase());
            new Thread(cli.getClientSocket()).start();
            new Thread(cli).start();
        }

    }
}
