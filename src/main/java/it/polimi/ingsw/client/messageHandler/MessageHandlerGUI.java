package it.polimi.ingsw.client.messageHandler;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.gamePhases.EndGamePhase;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.sentByServer.EndGameMessage;
import it.polimi.ingsw.messages.sentByServer.SendResourcesBoughtFromMarket;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.FourLeaderCardsMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.InitialResourcesMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.SendViewMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.StartGameMessage;
import it.polimi.ingsw.messages.sentByServer.updateMessages.*;
import it.polimi.ingsw.model.game.Game;
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
            gui.setAckArrived(true);
            gui.changeScene();

            if(gui.getGamePhase().equals(GamePhases.ASKACTIVEPRODUCTION)
                    ||gui.getGamePhase().equals(GamePhases.STORERESOURCES)
                    || gui.getOldScene().equals(gui.getScene(GUI.PRODUCTION_ZONE_CHOICE))){
                gui.setActionDone(true);
                //gui.setGamePhase(GamePhases.STARTGAME);
            }
           if(gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER)) gui.setActiveLeader(true);
            System.out.println(gui.isActionDone());
            System.out.println("ack");
        }
    }

    @Override
    public void handleMessage(NACKMessage message) {

        synchronized (gui) {
            gui.setNackArrived(true);
            gui.setErrorFromServer(message.getMessage());
           if(gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER)) {
               gui.setGamePhase(GamePhases.STARTGAME);
               gui.setActiveLeader(false);
           }
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
                gui.setOldScene(gui.getScene(GUI.START_GAME));
                gui.setCurrentScene(gui.getScene(GUI.START_GAME));
            } else {
                gui.setCurrentScene(gui.getScene(GUI.WAITING_ROOM));
                gui.setOldScene(gui.getScene(GUI.WAITING_ROOM));
                gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
            }

            gui.changeScene();

           /* if (clientSocket.getView().getActivePlayer().equals(clientSocket.getView().getNickname())) {
                gui.setGamePhase(GamePhases.MYTURN);
            } else gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);

            synchronized (gui) {
                gui.notifyAll();
            }
           */
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
            if (gui.getCurrentScene() == gui.getScene("START_GAME")) {
                gui.changeScene();
            }
        }
    }

    @Override
    public void handleUpdateMessage(UpdateDashBoardMessage message) {
        synchronized (gui) {
            clientSocket.getView().setDashboard(((UpdateDashBoardMessage) message).getDashboard());
            //if(gui.getCurrentScene() == gui.getScene("START_GAME")){
            gui.changeScene();
            System.out.println("Stock box 3: " + clientSocket.getView().getDashboard().getSerializableStock().getBoxes().get(2).length);
            //}
        }
    }

    @Override
    public void handleUpdateMessage(UpdateActivePlayerMessage message) {

        synchronized (gui) {
            if (clientSocket.getView().getNickname().equals(message.getMessage())) {
                gui.setGamePhase(GamePhases.STARTGAME);
            } else {

                gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);
            /*
            gui.setCurrentScene(gui.getScene(GUI.WAITING_ROOM));
            gui.setOldScene(gui.getScene(GUI.WAITING_ROOM));
            gui.setGamePhase(GamePhases.WAITINGOTHERPLAYERS);
             */
            }
            gui.setOldScene(gui.getScene(GUI.START_GAME));
            gui.setCurrentScene(gui.getScene(GUI.START_GAME));

            gui.changeScene();
        }
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

    }

    @Override
    public void handleMessage(EndGameMessage message) {
        synchronized (gui) {
            gui.getView().setScores(message.getScores());
            gui.getView().setWinners(message.getWinners());
            gui.setGamePhase(GamePhases.ENDGAME);
            gui.setCurrentScene(gui.getScene(GUI.ENDGAME));
            gui.changeScene();
        }
    }
}