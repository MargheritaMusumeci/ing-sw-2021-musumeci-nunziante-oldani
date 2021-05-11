package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.configurationMessages.FourLeaderCardsMessage;
import it.polimi.ingsw.messages.configurationMessages.InitialResourcesMessage;
import it.polimi.ingsw.messages.configurationMessages.SendViewMessage;
import it.polimi.ingsw.messages.configurationMessages.StartGameMessage;

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
            synchronized (cli){
                cli.notifyAll();
            }
            return;
        }

        if(message instanceof NACKMessage){
            cli.setIsNackArrived(true);

            synchronized (cli){
                cli.notifyAll();
            }

            return;
        }

        if (message instanceof ReconnectionMessage){
            System.out.println("riconnessione effettuata");
            cli.setIsAckArrived(true);
        }

        if(message instanceof StartGameMessage){
            System.out.println("gioco iniziato");
            cli.setGamePhase(GamePhases.INITIALRESOURCESELECTION);
        }

        if(message instanceof FourLeaderCardsMessage){
            System.out.println("Leader card ricevute");
            cli.setLeaderCards(((FourLeaderCardsMessage) message).getLeaderCards());
            cli.setGamePhase(GamePhases.INITIALLEADERCARDSELECTION);
        }

        if(message instanceof InitialResourcesMessage){
            System.out.println("Risorse iniziali ricevute");
            cli.setResources(((InitialResourcesMessage) message).getResources());
        }

        if(message instanceof SendViewMessage){
            System.out.println("ricevo la view iniziale");
            clientSocket.setView(((SendViewMessage) message).getView());

            if(clientSocket.getView().getActivePlayer().equals(clientSocket.getView().getNickname())){
                cli.setGamePhase(GamePhases.MYTURN);
            }else cli.setGamePhase(GamePhases.OTHERPLAYERSTURN);
        }
    }


}
