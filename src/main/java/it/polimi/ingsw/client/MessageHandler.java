package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.messages.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NACKMessage;
import it.polimi.ingsw.messages.PingMessage;

public class MessageHandler{

    private CLI cli;

    public MessageHandler( CLI cli){
        this.cli = cli;
    }

    /**
     * method able to handle basic messaged from the server and send to other method complex messages
     * @param message is the message that has to be handled
     */
    public void handleMessage(Message message){

        if(message instanceof PingMessage){

            return;
        }

        if(message instanceof ACKMessage){
            System.err.println("ack received");
            cli.setIsAckArrived(true);
            return;
        }

        if(message instanceof NACKMessage){
            System.err.println("nack received");
            cli.setIsNackArrived(true);
            cli.notifyAll();
            return;
        }

    }


}
