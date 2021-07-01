package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.sentByClient.ExitGameMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.*;
import it.polimi.ingsw.messages.sentByServer.*;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NumberOfPlayerMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.model.players.Player;

/**
 * class that defines the method to handle all the possible messages sent by the client
 */
public class MessageHandler {

    private final Server server;
    private final ServerClientConnection scc;

    /**
     * class constructor
     * @param server is the instance of the server
     * @param serverClientConnection is the server client connection that have will receive the messages
     */
    public MessageHandler(Server server, ServerClientConnection serverClientConnection){
        this.server = server;
        this.scc = serverClientConnection;
    }

    /**
     * method able to handle the configuration of the nickname for every player in the game
     * @param message containing the nickname
     */
    public void handleMessage(NickNameMessage message){
        if(scc.getGamePhase() == GamePhases.CONFIGURATION){
            //check the name validity
            if(message.getMessage().equals("LorenzoIlMagnifico")){
                scc.send(new NACKMessage("KO! This nickname cannot be used because is reserved for the unique and magnificent Lorenzo"));
                return;
            }

            if(message.getMessage().contains("/")){
                scc.send(new NACKMessage("You cannot use / character in your nickname"));
            }

            //check if the player has saved games due to persistence
            if(server.getPersistenceNicknameList().contains(message.getMessage())){
                //put the player in wait until all the player are back
                scc.send(new PersistenceMessage("wait for other players - server reconnection"));
                scc.setNickname(message.getMessage());
                server.updatePersistenceReconnections(message.getMessage(), scc);
                server.addTakenNickname(message.getMessage());
                return;
            }

            //check if the player was a disconnected one
            if(server.checkDisconnectedPlayer(message.getMessage()) != null){
                //handle the reconnection
                ServerClientConnection scc_temp = server.checkDisconnectedPlayer(message.getMessage());
                scc_temp.reconnect(scc.getSocket(), scc.getInputStream(), scc.getOutputStream());
                scc.setActive(false);
                server.removeWaitForReconnection(scc_temp);
                return;
            }
            if(!server.checkNickname(message.getMessage())){
                scc.send(new NACKMessage("Error! This nickname has already been taken"));
            } else {
                scc.send(new ACKMessage("OK"));
                scc.setNickname(message.getMessage());
                server.addTakenNickname(message.getMessage());
            }
        }else{
            scc.send(new NACKMessage("KO"));
        }

    }

    /**
     * method able to handle th configuration of the number of players the player wants to play with
     * @param message contains the number of players
     */
    public void handleMessage(NumberOfPlayerMessage message){
        if(scc.getGamePhase() == GamePhases.CONFIGURATION){
            if(scc.getNickname() == null){
                scc.send( new NACKMessage("Error! You have not chose your nickname yet"));
            }else{
                switch (Integer.parseInt(message.getMessage())){
                    case 1:
                        server.startSoloGame(scc);
                        break;
                    case 2:
                        server.addToLobby2Players(scc);
                        break;
                    case 3:
                        server.addToLobby3Players(scc);
                        break;
                    case 4:
                        server.addToLobby4Players(scc);
                        break;
                    default:
                        scc.send(new NACKMessage("Error! Invalid number o player, rang is between 1 and 4"));
                        return;
                }

                scc.setGamePhase(GamePhases.INITIALIZATION);
                scc.send(new ACKMessage("OK"));

            }
        }else{
            scc.send(new NACKMessage("KO"));
        }
    }

    /**
     * method able to handle the initialization of the leader cards that a player has to choose
     * @param message contains an arraylist of 4 leader cards from which the player have to choose
     */
    public void handleMessage(LeaderCardChoiceMessage message){
        if(scc.getGamePhase() == GamePhases.INITIALIZATION){
            if(scc.getGameHandler().getInitializationHandler().setLeaderCards( scc.getGameHandler().getPlayersInGame().get(scc),
                    message.getLeaderCards())){
                scc.send(new ACKMessage("OK"));
                //check if alla the player has 2 leader cards
                for(ServerClientConnection serverClientConnection : scc.getGameHandler().getPlayersInGame().keySet()){
                    if(serverClientConnection.getGameHandler().getPlayersInGame().get(serverClientConnection).getDashboard().getLeaderCards().size() != 2){
                        return;
                    }
                }
                scc.getGameHandler().handleInitialResourcesSettings();

                if(scc.getGameHandler().getNumberOfPlayers() == 1)
                    scc.getGameHandler().initializationView();
            }

            else
                scc.send(new NACKMessage("KO"));
        }else{
            scc.send(new NACKMessage("KO"));
        }
    }

    /**
     * method that stores the resources selected at the beginning of the game from the players that
     * don't have the inkwell
     * @param message contains the resources chosen
     */
    public void handleMessage(SelectedInitialResourceMessage message){
        if(scc.getGamePhase() == GamePhases.INITIALIZATION){
            if (scc.getGameHandler().getInitializationHandler().setInitialResources(scc.getGameHandler().getPlayersInGame().get(scc),
                    message.getResources())) {

                //TODO verify the follow
                //Increase the initial position of the player if he is the number 3 or 4
                scc.getGameHandler().getInitializationHandler().setInitialPositionInPopeTrack(
                        scc.getGameHandler().getPlayersInGame().get(scc)
                );

                scc.send(new ACKMessage("OK"));
                for (Player player : scc.getGameHandler().getPlayersInGame().values()) {
                    if (!player.getDashboard().getInkwell() && player.getDashboard().getStock().stockIsEmpty()) {
                        return;
                    }
                }
                scc.getGameHandler().initializationView();
            }else{
                scc.send(new NACKMessage("KO-1"));
            }

        }else{
            scc.send(new NACKMessage("KO-2"));
        }
    }

    /**
     * method able to handle the action of buying from the market
     * @param message contains the position in which resources have to be taken
     */
    public void handleActionMessage(BuyFromMarketMessage message){
           if (checkAction()) scc.send(scc.getGameHandler().getTurnHandler().doAction(message));
    }

    /**
     * method able to handle to action of storing the resources in the stock after a buyFromMarket action
     * @param message contains the resources that the player has chosen to actually store
     */
    public void handleActionMessage(StoreResourcesMessage message){
        if (checkAction()) scc.send(scc.getGameHandler().getTurnHandler().doAction(message));
    }

    /**
     * method able to handle the action of buying an evolution card from the evolution section
     * @param message contains the card's position in the evolution section
     */
    public void handleActionMessage(BuyEvolutionCardMessage message){
        if (checkAction()) scc.send(scc.getGameHandler().getTurnHandler().doAction(message));
    }

    /**
     * method able to handle the action of activating the productions
     * @param message contains all the production that should be activated and also contains the resources
     *                needed to activate the basic production zone and the possibile leader productions
     */
    public void handleActionMessage(ActiveProductionMessage message){
        if (checkAction()) scc.send(scc.getGameHandler().getTurnHandler().doAction(message));
    }

    /**
     * method able to handle the action of active a leader card
     * @param message contains the position of the leader card in the set of the player
     */
    public void handleActionMessage(ActiveLeaderCardMessage message){
        if (checkAction()) scc.send(scc.getGameHandler().getTurnHandler().doAction(message));
    }

    /**
     * method able to handle the action of discard a leader card
     * @param message contains the position of the leader card to be discarded
     */
    public void handleActionMessage(DiscardLeaderCardMessage message){
        if (checkAction()) scc.send(scc.getGameHandler().getTurnHandler().doAction(message));
    }

    /**
     * method able to handle the action of requesting resources bought from market
     * @param message does not contain useful information
     */
    public void handleActionMessage(RequestResourcesBoughtFromMarketMessage message){
        if(checkAction()) scc.send(new SendResourcesBoughtFromMarket("Resources",scc.getGameHandler().getPlayersInGame().get(scc).getResources()));
    }

    /**
     * method that checks if the player is in the "GAME" phase when trying to make an action
     * @return true if the action can be done, false otherwise
     */
    private boolean checkAction(){
        if (scc.getGamePhase() != GamePhases.GAME){
            scc.send(new NACKMessage("Error! You are not in the correct phase of the game"));
            return false;
        }
        //check if the player requesting the action is the active player
        if(!scc.getNickname().equals(scc.getGameHandler().getGame().getActivePlayer().getNickName())){
            scc.send(new NACKMessage("Error! It's not your turn, is the turn of: " + scc.getGameHandler().getGame().getActivePlayer().getNickName()));
            return false;
        }
        return true;
    }

    /**
     * method able to notify all the player that the active player has changed because someone has ended his turn
     *
     */
    public void handleMessage(EndTurnMessage message) {
        if (scc.getGamePhase() == GamePhases.GAME) {

            //store game status
            server.getPersistence().saveGame(scc.getGameHandler().getGame());

            //send the message to change turn to every player
            Message messageEndTurn = scc.getGameHandler().getTurnHandler().endTurn();
            if (messageEndTurn instanceof UpdateActivePlayerMessage) {
                for (ServerClientConnection serverClientConnection : scc.getGameHandler().getPlayersInGame().keySet()) {
                    serverClientConnection.send(messageEndTurn);
                }
            } else if (messageEndTurn instanceof EndGameMessage) {
                for (ServerClientConnection serverClientConnection : scc.getGameHandler().getPlayersInGame().keySet()) {
                    serverClientConnection.send(messageEndTurn);
                }
            }


        } else {
            scc.send(new NACKMessage("KO"));
        }


    }

    /**
     * method that handles the message that confirms to the gameHandler that a player has recived the end game message
     * @param exitGameMessage does not contains useful information
     */
    public void handleMessage(ExitGameMessage exitGameMessage) {
        scc.getGameHandler().endGame(scc);
    }
}
