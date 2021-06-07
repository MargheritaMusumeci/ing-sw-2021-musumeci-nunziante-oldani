package it.polimi.ingsw.client.messageHandler;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.ViewPlayerController;
import it.polimi.ingsw.client.GUI.GameFxml;
import it.polimi.ingsw.client.GUI.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.messages.sentByServer.*;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.*;
import it.polimi.ingsw.messages.sentByServer.updateMessages.*;
import it.polimi.ingsw.model.game.Resource;

public class MessageHandlerGUI extends MessageHandler {

    private final GUI gui;

    public MessageHandlerGUI(GUI gui, ClientSocket clientSocket) {
        this.gui = gui;
        this.clientSocket = clientSocket;
    }

    @Override
    public void handleMessage(ACKMessage message) {
        synchronized (gui) {

            if(gui.getGamePhase().equals(GamePhases.ASKACTIVEPRODUCTION)
                    || gui.getOldScene().equals(gui.getScene(GameFxml.PRODUCTION_ZONE_CHOICE.s))){
                gui.setActionDone(true);
            }

            if(gui.getGamePhase().equals(GamePhases.STORERESOURCES)||gui.getGamePhase().equals(GamePhases.CHOOSEWHITEBALL)){
                gui.getClientSocket().send(new RequestResourcesBoughtFromMarketMessage(""));
                gui.setActionDone(true);
                return;
            }

            if(gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER)){
                if(gui.isUpdateDashboardArrived()){
                    gui.setUpdateDashboardArrived(false);
                    gui.setGamePhase(GamePhases.STARTGAME);
                    System.out.println("Calling activeLeaderAck after received the ack");
                    ((ViewPlayerController) gui.getController("newView.fxml")).activeLeaderACK();
                }
                else{
                    gui.setAckArrived(true);
                    return;
                }
            }

            gui.changeScene();
            System.out.println(gui.isActionDone());
            System.out.println("ack");
        }
    }

    @Override
    public void handleMessage(NACKMessage message) {

        synchronized (gui) {
            gui.setErrorFromServer(message.getMessage());
           if(gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER) && gui.isUpdateDashboardArrived()){
               gui.setUpdateDashboardArrived(false);
               gui.setGamePhase(GamePhases.STARTGAME);
           }else if(gui.getGamePhase().equals(GamePhases.STARTGAME)){
               gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
           }
            else{
                gui.setNackArrived(true);
                gui.setGamePhase(gui.phase(gui.getOldScene()));
            }
            gui.setCurrentScene(gui.getOldScene());
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(ReconnectionMessage message) {
        gui.setView(message.getView());
        if(gui.getView().getNickname().equals(gui.getView().getActivePlayer())){
            gui.setGamePhase(GamePhases.MYTURN);
            gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
            gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));
        }
        else{
            gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);
            gui.setOldScene(gui.getScene(GameFxml.OTHERTURN.s));
            gui.setCurrentScene(gui.getScene(GameFxml.OTHERTURN.s));
        }
        gui.changeScene();
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
            gui.setCurrentScene(gui.getScene(GameFxml.LEADER_CARD.s));
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(InitialResourcesMessage message) {

        synchronized (gui) {
            gui.setResources(message.getResources());
            gui.setGamePhase(GamePhases.INITIALRESOURCESELECTION);
            gui.setOldScene(gui.getCurrentScene());
            gui.setCurrentScene(gui.getScene(GameFxml.INITIAL_RESOURCES.s));
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(SendViewMessage message) {

        synchronized (gui) {
            System.out.println("start game");
            clientSocket.setView(message.getView());
            gui.setView(message.getView());
            //gui.setGamePhase(GamePhases.STARTGAME);
            //gui.setOldScene(gui.getScene(GUI.START_GAME));
            //gui.setCurrentScene(gui.getScene(GUI.START_GAME));
            //gui.changeScene();

            if (clientSocket.getView().getActivePlayer().equals(clientSocket.getView().getNickname())) {
                gui.setGamePhase(GamePhases.STARTGAME);
                gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
                gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));
            } else {
                gui.setCurrentScene(gui.getScene(GameFxml.WAITING_ROOM.s));
                gui.setOldScene(gui.getScene(GameFxml.WAITING_ROOM.s));
                gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
            }
            gui.changeScene();
        }
    }

    @Override
    public void handleMessage(SendResourcesBoughtFromMarket message) {

        synchronized (gui) {
            clientSocket.getView().setResourcesBoughtFromMarker(message.getResources());
            gui.changeScene();
            System.out.println("risorse");
            for (Resource res:clientSocket.getView().getResourcesBoughtFromMarker()) {
                System.out.println(res.name());
            }
        }
    }

    @Override
    public void handleUpdateMessage(UpdateLeaderCardsMessage message) {
        synchronized (gui) {
            clientSocket.getView().setLeaderCards(((UpdateLeaderCardsMessage) message).getLeaderCards());
            if(gui.getCurrentScene() == gui.getScene("START_GAME")) {
                gui.setGamePhase(GamePhases.STARTGAME);
                gui.changeScene();
            }
        }
    }

    @Override
    public void handleUpdateMessage(UpdateDashBoardMessage message) {
        synchronized (gui) {

            clientSocket.getView().setDashboard(((UpdateDashBoardMessage) message).getDashboard());
            //if(gui.getCurrentScene() == gui.getScene("START_GAME")){
            if(gui.getGamePhase().equals(GamePhases.ASKACTIVEPRODUCTION)) gui.setActionDone(true);

            if(!gui.getGamePhase().equals(GamePhases.STORERESOURCES)){

                if(gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER)){
                    if(gui.isAckArrived() || gui.isNackArrived()){
                        gui.setAckArrived(false);
                        gui.setNackArrived(false);
                        System.out.println("Calling activeLeaderAck after received the ack");
                        ((ViewPlayerController) gui.getController("newView.fxml")).activeLeaderACK();
                    }
                    else{
                        gui.setUpdateDashboardArrived(true);
                        return;
                    }
                }
                gui.setGamePhase(GamePhases.STARTGAME);
                gui.changeScene();
            }
        }
    }

    @Override
    public void handleUpdateMessage(UpdateActivePlayerMessage message) {

        synchronized (gui) {
            if (clientSocket.getView().getNickname().equals(message.getMessage())) {
                gui.setGamePhase(GamePhases.STARTGAME);
            } else {

                gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);
            }
            gui.setOldScene(gui.getScene(GameFxml.START_GAME.s));
            gui.setCurrentScene(gui.getScene(GameFxml.START_GAME.s));

            gui.changeScene();
        }
    }

    @Override
    public void handleUpdateMessage(UpdateEvolutionSectionMessage message) {
        synchronized (gui) {
            clientSocket.getView().setEvolutionSection(((UpdateEvolutionSectionMessage) message).getEvolutionSection());
            if (gui.getCurrentScene() == gui.getScene("START_GAME")) {
                gui.changeScene();
            }
        }
    }

    @Override
    public void handleUpdateMessage(UpdateMarketMessage message) {
        synchronized (gui) {
            clientSocket.getView().setMarket(((UpdateMarketMessage) message).getMarket());
            if (gui.getCurrentScene() == gui.getScene("START_GAME")) {
                gui.changeScene();
            }
        }
    }

    @Override
    public void handleUpdateMessage(UpdateOtherPlayerViewMessage message) {
        gui.getView().setEnemyDashboard(message.getView().getDashboard(), message.getNickname());
        gui.getView().setEnemyActivatedLeaderCards(message.getView().getDashboard(), message.getView().getLeaderCards());
    }

    @Override
    public void handleMessage(AbortGameMessage abortGameMessage) {

    }

    @Override
    public void handleMessage(MarketAndEvolutionSectionMessage marketAndEvolutionSectionMessage) {
        gui.setMarket(marketAndEvolutionSectionMessage.getSerializableMarket());
        gui.setEvolutionSection(marketAndEvolutionSectionMessage.getSerializableEvolutionSection());
    }

    @Override
    public void handleMessage(EndGameMessage message) {
        synchronized (gui) {
            gui.getView().setScores(message.getScores());
            gui.getView().setWinners(message.getWinners());
            gui.setGamePhase(GamePhases.ENDGAME);
            gui.setCurrentScene(gui.getScene(GameFxml.ENDGAME.s));
            gui.changeScene();
        }
    }
}