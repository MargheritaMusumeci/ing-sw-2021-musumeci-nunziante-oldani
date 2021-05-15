package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.actionMessages.SendResourcesBoughtFromMarket;
import it.polimi.ingsw.messages.configurationMessages.FourLeaderCardsMessage;
import it.polimi.ingsw.messages.configurationMessages.InitialResourcesMessage;
import it.polimi.ingsw.messages.configurationMessages.SendViewMessage;
import it.polimi.ingsw.messages.configurationMessages.StartGameMessage;
import it.polimi.ingsw.messages.updateMessages.*;

public class MessageHandlerGUI extends MessageHandler {

    private final GUI gui;

    public MessageHandlerGUI(GUI gui, ClientSocket clientSocket) {
        this.gui = gui;
        this.clientSocket = clientSocket;
    }

    /**
     * method able to handle basic messaged from the server and send to other method complex messages
     *
     * @param message is the message that has to be handled
     */
    public void handleMessage(Message message) {

        if (message instanceof UpdateMessage) {
            // handleUpdateMessage((UpdateMessage) message);
        }

        if (message instanceof PingMessage) {
            clientSocket.send(new PingMessage("Ping response"));
            return;
        }

        if(message instanceof ACKMessage){
            //gui.setAckArrived(true);
            synchronized (gui){
                gui.notifyAll();
            }
            return;
        }

        if(message instanceof NACKMessage){
            //gui.setNackArrived(true);

            synchronized (gui){
                gui.notifyAll();
            }

            return;
        }
        /*
        if (message instanceof ReconnectionMessage) {
            //gui.setIsAckArrived(true);
        }

        if (message instanceof StartGameMessage) {
            gui.setGamePhase(GamePhases.INITIALRESOURCESELECTION);
        }

        if (message instanceof FourLeaderCardsMessage) {
            //gui.setLeaderCards(((FourLeaderCardsMessage) message).getLeaderCards());
            gui.setGamePhase(GamePhases.INITIALLEADERCARDSELECTION);

            synchronized (gui) {
                gui.notifyAll();
            }
        }

        if (message instanceof InitialResourcesMessage) {
            //gui.setResources(((InitialResourcesMessage) message).getResources());
        }

        if (message instanceof SendViewMessage) {
            clientSocket.setView(((SendViewMessage) message).getView());

            if (clientSocket.getView().getActivePlayer().equals(clientSocket.getView().getNickname())) {
                gui.setGamePhase(GamePhases.MYTURN);
            } else gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);

            synchronized (gui) {
                gui.notifyAll();
            }
        }

        if (message instanceof SendResourcesBoughtFromMarket) {
            clientSocket.getView().setResourcesBoughtFromMarker(((SendResourcesBoughtFromMarket) message).getResources());
            synchronized (gui) {
                gui.notifyAll();
            }
        }
    }

    private void handleUpdateMessage(UpdateMessage message) {

        if (message instanceof UpdateLeaderCardsMessage) {
            clientSocket.getView().setLeaderCards(((UpdateLeaderCardsMessage) message).getLeaderCards());
        }

        if (message instanceof UpdateDashBoardMessage) {
            clientSocket.getView().setDashboard(((UpdateDashBoardMessage) message).getDashboard());
        }

        if (message instanceof UpdateActivePlayerMessage) {
            clientSocket.getView().setActivePlayer(message.getMessage());
            if (clientSocket.getView().getNickname().equals(clientSocket.getView().getActivePlayer())) {
                //allora è il mio turno
                gui.setGamePhase(GamePhases.MYTURN);
                synchronized (gui) {
                    gui.notifyAll();
                }
            } else {
                //allora è il tuno dei miei avversari
                gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);
            }
        }

        if (message instanceof UpdateEvolutionSectionMessage) {
            clientSocket.getView().setEvolutionSection(((UpdateEvolutionSectionMessage) message).getEvolutionSection());
        }

        if (message instanceof UpdateMarketMessage) {
            clientSocket.getView().setMarket(((UpdateMarketMessage) message).getMarket());
        }
*/

    }
}
