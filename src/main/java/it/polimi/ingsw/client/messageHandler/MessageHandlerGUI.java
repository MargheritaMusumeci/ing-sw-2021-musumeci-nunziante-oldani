package it.polimi.ingsw.client.messageHandler;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.sentByServer.SendResourcesBoughtFromMarket;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.FourLeaderCardsMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.InitialResourcesMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.SendViewMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.StartGameMessage;
import it.polimi.ingsw.messages.sentByServer.updateMessages.*;

public class MessageHandlerGUI extends MessageHandler {

    private final GUI gui;

    public MessageHandlerGUI(GUI gui, ClientSocket clientSocket) {
        this.gui = gui;
        this.clientSocket = clientSocket;
    }

    @Override
    public void handleMessage(ACKMessage message) {
        synchronized (gui) {
            gui.setAckArrived(true);
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(NACKMessage message) {

        synchronized (gui) {
            gui.setNackArrived(true);
            gui.setErrorFromServer(message.getMessage());
            //if leadercard fase, replace it with startgame
            gui.setGamePhase(gui.phase(gui.getOldScene()));
            gui.setCurrentScene(gui.getOldScene());
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(ReconnectionMessage message) {

    }

    @Override
    public void handleMessage(StartGameMessage message) {

        //gui.setGamePhase(GamePhases.INITIALRESOURCESELECTION);
    }

    @Override
    public void handleMessage(FourLeaderCardsMessage message) {

        synchronized (gui) {
            gui.setLeaderCards(message.getLeaderCards());
            gui.setGamePhase(GamePhases.INITIALLEADERCARDSELECTION);
            gui.setOldScene(gui.getCurrentScene());
            gui.setCurrentScene(gui.getScene(GUI.LEADER_CARD));
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(InitialResourcesMessage message) {

        synchronized (gui) {
            gui.setResources(message.getResources());
            gui.setGamePhase(GamePhases.INITIALRESOURCESELECTION);
            gui.setOldScene(gui.getCurrentScene());
            gui.setCurrentScene(gui.getScene(GUI.INITIAL_RESOURCES));
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(SendViewMessage message) {

        System.out.println("start game");
        clientSocket.setView(message.getView());
        gui.setView(message.getView());
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setCurrentScene(gui.getScene(GUI.START_GAME));
        gui.changeScene();

           /* if (clientSocket.getView().getActivePlayer().equals(clientSocket.getView().getNickname())) {
                gui.setGamePhase(GamePhases.MYTURN);
            } else gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);

            synchronized (gui) {
                gui.notifyAll();
            }
           */
    }

    @Override
    public void handleMessage(SendResourcesBoughtFromMarket message) {

        clientSocket.getView().setResourcesBoughtFromMarker(message.getResources());
        synchronized (gui) {
            gui.changeScene();
        }
    }

    @Override
    public void handleUpdateMessage(UpdateLeaderCardsMessage message) {
        clientSocket.getView().setLeaderCards(((UpdateLeaderCardsMessage) message).getLeaderCards());
        if(gui.getGamePhase()==GamePhases.STARTGAME){
            gui.changeScene();
        }
    }

    @Override
    public void handleUpdateMessage(UpdateDashBoardMessage message) {
        clientSocket.getView().setDashboard(((UpdateDashBoardMessage) message).getDashboard());
        if(gui.getGamePhase()==GamePhases.STARTGAME){
            gui.changeScene();
        }
    }

    @Override
    public void handleUpdateMessage(UpdateActivePlayerMessage message) {
        /*
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
            }*/

    }

    @Override
    public void handleUpdateMessage(UpdateEvolutionSectionMessage message) {
        //clientSocket.getView().setEvolutionSection(((UpdateEvolutionSectionMessage) message).getEvolutionSection());
    }

    @Override
    public void handleUpdateMessage(UpdateMarketMessage message) {
        clientSocket.getView().setMarket(((UpdateMarketMessage) message).getMarket());
        if(gui.getGamePhase()==GamePhases.STARTGAME){
            gui.changeScene();
        }
    }
}