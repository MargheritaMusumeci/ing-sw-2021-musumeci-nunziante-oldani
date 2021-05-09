package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.messages.*;

public class MessageHandler{

    private CLI cli;
    private ClientSocket clientSocket;

    public MessageHandler(CLI cli, ClientSocket clientSocket){
        this.cli = cli;
        this.clientSocket = clientSocket;
    }

    /**
     * method able to handle basic messaged from the server and send to other method complex messages
     * @param message is the message that has to be handled
     */
    public void handleMessage(Message message){

        if(message instanceof PingMessage){
            clientSocket.send(new PingMessage("Ping response"));
            return;
        }

        if(message instanceof ACKMessage){
            cli.setIsAckArrived(true);
            return;
        }

        if(message instanceof NACKMessage){
            cli.setIsNackArrived(true);
            return;
        }

        if (message instanceof ReconnectionMessage){
            System.out.println("riconnessione effettuata");
            cli.setIsAckArrived(true);
        }

    }


}
