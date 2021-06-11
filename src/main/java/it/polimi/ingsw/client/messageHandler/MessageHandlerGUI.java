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

    /**
     * The action that the user asked the server to perform was successful
     */
    @Override
    public void handleMessage(ACKMessage message) {
        synchronized (gui) {

            if(gui.getGamePhase().equals(GamePhases.ASKACTIVEPRODUCTION)||
                    gui.getOldScene().equals(gui.getScene(GameFxml.PRODUCTION_ZONE_CHOICE.s))){
                    gui.setActionDone(true);
            }

            if(gui.getGamePhase().equals(GamePhases.STORERESOURCES)|| gui.getGamePhase().equals(GamePhases.CHOOSEWHITEBALL)){
                gui.getClientSocket().send(new RequestResourcesBoughtFromMarketMessage(""));
                gui.setActionDone(true);
            }

            if(gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER)){
                if(gui.isUpdateDashboardArrived()){
                    gui.setUpdateDashboardArrived(false);
                    gui.setGamePhase(GamePhases.MYTURN);
                    System.out.println("Calling activeLeaderAck after received the ack");
                    ((ViewPlayerController) gui.getController(GameFxml.MY_TURN.s)).activeLeaderACK();
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

    /**
     * The action that the user asked the server to perform was unsuccessful:
     1) buy from market // store resources // choose white balls --> load old scene
     2) buy evolution card // store evolution card --> load old scene
     3) active production --> load main scene
     4) active leader // discard leader --> load main scene
     5) choose basic production // leader production --> load old scene
     6) see other view --> load other scene
     * @param message empty
     */
    @Override
    public void handleMessage(NACKMessage message) {

        synchronized (gui) {
            gui.setErrorFromServer(message.getMessage());
           if(gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER) || gui.getGamePhase().equals(GamePhases.ASKACTIVEPRODUCTION) ){
               gui.setUpdateDashboardArrived(false);
               gui.setGamePhase(GamePhases.MYTURN);
               gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
           }else if(gui.getGamePhase().equals(GamePhases.MYTURN)){
               gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
           }
            else{
                gui.setNackArrived(true);
                gui.setGamePhase(gui.phase(gui.getOldScene()));
            }
            gui.setCurrentScene(gui.getOldScene());
            gui.changeScene();
        }
    }

    /**
     * The player can join the game again:
     * if he is the active player, game phase will be "MY_TURN"
     * if he is not the active player, game phase will be "OTHER_TURN"
     * @param message contains the view of the player and the number of player in the game
     */
    @Override
    public void handleMessage(ReconnectionMessage message) {
        gui.setView(message.getView());
        gui.setPlayers(message.getNumberOfPlayers());
        gui.setLeaderCards(message.getView().getLeaderCards());

        if(gui.getNickname().equals(message.getView().getActivePlayer())){
            gui.setGamePhase(GamePhases.MYTURN);
            gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
            gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
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
        //useless ??
        System.out.println("ho inviato il messaggio inutile ? ");
    }

    /**
     * When there are enough players in the game lobby, the game starts and the server notifies the departure
     * by sending the 4 leader cards.
     * When the message arrives, the scene is changed.
     * @param message the four leader cards among which the user must choose
     */
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

    /**
     * After all the players have chosen the leader cards the server goes on and sends the resources.
     * When the message arrives, the scene is changed.
     * @param message the resources among which the user must choose
     */
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

    /**
     * The server communicates the start of the challenge by sending the user's view.
     * Based on the active player is set the phase.
     * @param message client view
     */
    @Override
    public void handleMessage(SendViewMessage message) {

        synchronized (gui) {
            System.out.println("start game");
            gui.setView(message.getView());

            if (gui.getView().getActivePlayer().equals(gui.getView().getNickname())) {
                gui.setGamePhase(GamePhases.MYTURN);
                gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
                gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
            } else {
                gui.setCurrentScene(gui.getScene(GameFxml.OTHERTURN.s));
                gui.setOldScene(gui.getScene(GameFxml.OTHERTURN.s));
                gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);
            }
            gui.changeScene();
        }
    }

    /**
     * Receipt of resources bought from the market
     * @param message resources
     */
    @Override
    public void handleMessage(SendResourcesBoughtFromMarket message) {

        synchronized (gui) {
            gui.getView().setResourcesBoughtFromMarker(message.getResources());
            gui.changeScene();
            System.out.println("risorse");
            for (Resource res:gui.getView().getResourcesBoughtFromMarker()) {
                System.out.println(res.name());
            }
        }
    }

    /**
     * Message received by the client after asking to activate or discard a leader card
     * @param message new version of leader card array
     */
    @Override
    public void handleUpdateMessage(UpdateLeaderCardsMessage message) {
        synchronized (gui) {
            gui.getView().setLeaderCards(message.getLeaderCards());
            if(gui.getCurrentScene() == gui.getScene(GameFxml.MY_TURN.s)) {
                gui.setGamePhase(GamePhases.MYTURN);
                gui.changeScene();
            }
        }
    }

    /**
     * Something about the model dashboard has changed and needs to be updated in the view
     * @param message contains all dashboard elements
     */
    @Override
    public void handleUpdateMessage(UpdateDashBoardMessage message) {

        synchronized (gui) {
            gui.getView().setDashboard(message.getDashboard());
            if (gui.getGamePhase().equals(GamePhases.ASKACTIVEPRODUCTION)) gui.setActionDone(true);
            //TODO qua e non in update leader ???
            if (gui.getGamePhase().equals(GamePhases.ASKACTIVELEADER)) {
                if (gui.isAckArrived() || gui.isNackArrived()) {
                    gui.setAckArrived(false);
                    gui.setNackArrived(false);
                    System.out.println("Calling activeLeaderAck after received the ack");

                    ((ViewPlayerController) gui.getController("newView.fxml")).activeLeaderACK();
                } else {
                    gui.setUpdateDashboardArrived(true);
                    return;
                }
            }

            if (gui.getCurrentScene() == gui.getScene(GameFxml.MY_TURN.s) ||
                    gui.getCurrentScene() == gui.getScene(GameFxml.OTHERVIEW.s)) {
                gui.setGamePhase(GamePhases.MYTURN);
            }
            gui.changeScene();
        }
    }

    /**
     * The server reports that a new game round has ended / started
     * @param message empty
     */
    @Override
    public void handleUpdateMessage(UpdateActivePlayerMessage message) {
        synchronized (gui) {
            if (gui.getView().getNickname().equals(message.getMessage())) {
                gui.setGamePhase(GamePhases.MYTURN);
            } else {
                gui.setGamePhase(GamePhases.OTHERPLAYERSTURN);
            }
            gui.setOldScene(gui.getScene(GameFxml.MY_TURN.s));
            gui.setCurrentScene(gui.getScene(GameFxml.MY_TURN.s));
            gui.changeScene();
        }
    }

    /**
     * Someone bought from the evolution section and therefore needs to be updated
     * @param message copy of market
     */
    @Override
    public void handleUpdateMessage(UpdateEvolutionSectionMessage message) {
        synchronized (gui) {
            gui.getView().setEvolutionSection(message.getEvolutionSection());
            if (gui.getCurrentScene() == gui.getScene(GameFxml.MY_TURN.s) ||
                    gui.getCurrentScene() == gui.getScene(GameFxml.OTHERVIEW.s) ||
                    gui.getCurrentScene() == gui.getScene(GameFxml.EVOLUTION_SECTION.s)) {
                gui.changeScene();
            }
        }
    }

    /**
     * Someone bought from the market and therefore needs to be updated
     * @param message copy of market
     */
    @Override
    public void handleUpdateMessage(UpdateMarketMessage message) {
        synchronized (gui) {
            gui.getView().setMarket(message.getMarket());
            if (gui.getCurrentScene() == gui.getScene(GameFxml.MY_TURN.s) ||
                    gui.getCurrentScene() == gui.getScene(GameFxml.OTHERVIEW.s) ||
                    gui.getCurrentScene() == gui.getScene(GameFxml.MARKET.s)) {
                gui.changeScene();
            }
        }
    }

    /**
     * Update the view of other players
     * @param message contains the view and the nickname of the player the message refers to
     */
    @Override
    public void handleUpdateMessage(UpdateOtherPlayerViewMessage message) {
        synchronized (gui) {
            gui.getView().setEnemyDashboard(message.getView().getDashboard(), message.getNickname());
            gui.getView().setEnemyActivatedLeaderCards(message.getView().getDashboard(), message.getView().getLeaderCards());
        }
    }

    @Override
    public void handleMessage(AbortGameMessage abortGameMessage) {}

    /**
     * Initial market and evolution section sent before the view is created.
     * Necessary for the choice of leader cards and resources
     * @param marketAndEvolutionSectionMessage message that contains a copy of market and evolution section
     */
    @Override
    public void handleMessage(MarketAndEvolutionSectionMessage marketAndEvolutionSectionMessage) {
        synchronized (gui) {
            gui.setMarket(marketAndEvolutionSectionMessage.getSerializableMarket());
            gui.setEvolutionSection(marketAndEvolutionSectionMessage.getSerializableEvolutionSection());
        }
    }

    /**
     * The server reports that the game is over
     * @param message empty
     */
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