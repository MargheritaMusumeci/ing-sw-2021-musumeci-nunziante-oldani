package it.polimi.ingsw.client.gamePhases;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.UI;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class InitializationPhase extends Phase{
    @Override
    public void makeAction(CLI cli) {

        Scanner scanner = new Scanner(System.in);
        int port;
        String address;

        cli.printTitle();

        System.out.print("Enter the ip address of the Server: ");
        address = scanner.next();

        System.out.print("Enter the port: ");
        port = scanner.nextInt();

        while(port < 1025 || port > 65535){
            System.out.println("Invalid port number. Pick a porta in range 1025-65535");
            System.out.print("Enter the port: ");
            port = scanner.nextInt();
        }

        try {
            cli.setSocket(new Socket(address, port));
            cli.setClientSocket(new ClientSocket(cli, cli.getSocket()));
        } catch (IOException e) {
            cli.setClientSocket(null);
            System.out.println("There was a problem with the server. Please chek if the ip address and port number" +
                    "are correct and if the server is up and running ");
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
