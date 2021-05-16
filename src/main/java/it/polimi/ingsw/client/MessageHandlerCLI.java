package it.polimi.ingsw.client;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.actionMessages.SendResourcesBoughtFromMarket;
import it.polimi.ingsw.messages.configurationMessages.FourLeaderCardsMessage;
import it.polimi.ingsw.messages.configurationMessages.InitialResourcesMessage;
import it.polimi.ingsw.messages.configurationMessages.SendViewMessage;
import it.polimi.ingsw.messages.configurationMessages.StartGameMessage;
import it.polimi.ingsw.messages.updateMessages.*;

public class MessageHandlerCLI extends MessageHandler{

    private CLI cli;

    public MessageHandlerCLI(CLI cli, ClientSocket clientSocket){
        this.cli = cli;
        this.clientSocket = clientSocket;
    }

    /**
     * method able to handle basic messaged from the server and send to other method complex messages
     * @param message is the message that has to be handled
     */
    public void handleMessage(Message message){

        if(message instanceof UpdateMessage){
            handleUpdateMessage((UpdateMessage) message);
        }

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

            synchronized (cli){
                cli.notifyAll();
            }
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

            synchronized (cli){
                cli.notifyAll();
            }
        }

        if(message instanceof SendResourcesBoughtFromMarket){
            clientSocket.getView().setResourcesBoughtFromMarker(((SendResourcesBoughtFromMarket) message).getResources());
            synchronized (cli){
                cli.notifyAll();
            }
        }
    }

    private void handleUpdateMessage(UpdateMessage message) {

        if(message instanceof UpdateLeaderCardsMessage){
            clientSocket.getView().setLeaderCards(((UpdateLeaderCardsMessage) message).getLeaderCards());
        }

        if(message instanceof UpdateDashBoardMessage){
            clientSocket.getView().setDashboard(((UpdateDashBoardMessage) message).getDashboard());
        }

        if (message instanceof UpdateActivePlayerMessage){
            clientSocket.getView().setActivePlayer(message.getMessage());
            if(clientSocket.getView().getNickname().equals(clientSocket.getView().getActivePlayer())){
                //It's my turn
                cli.setGamePhase(GamePhases.MYTURN);
            }else{
                //It's my enemies turn
                cli.setGamePhase(GamePhases.OTHERPLAYERSTURN);
            }

            synchronized (cli){
                cli.notifyAll();
            }
        }

        if(message instanceof UpdateEvolutionSectionMessage){
            clientSocket.getView().setEvolutionSection(((UpdateEvolutionSectionMessage) message).getEvolutionSection());
        }

        if(message instanceof UpdateMarketMessage){
            clientSocket.getView().setMarket(((UpdateMarketMessage) message).getMarket());
        }

    }

}
