package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActionMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.messages.sentByServer.SendResourcesBoughtFromMarket;
import it.polimi.ingsw.messages.sentByClient.EndTurnMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NickNameMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.NumberOfPlayerMessage;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.SelectedInitialResourceMessage;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.model.players.Player;

public class MessageHandler {

    private Server server;
    private ServerClientConnection scc;

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
            //controllo che il giocatore non sia gia un giocatore di quelli che attendevano di riconnettersi
            if(server.checkDisconnectedPlayer(message.getMessage()) != null){
                //gestisco la riconnessione
                ServerClientConnection scc_temp = server.checkDisconnectedPlayer(message.getMessage());
                scc_temp.reconnect(scc.getSocket());
                //dovrei elimanre dalla hashmap questa virtual view
                server.removeWaitForReconnection(scc_temp);
                //send reconnection Message
                scc_temp.send(new ReconnectionMessage("Re-entering the game"));
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
                //controllare che tutti i giocatori del gamehandler abbiamo un arraylist di leaderCaard con size 2
                for(ServerClientConnection serverClientConnection : scc.getGameHandler().getPlayersInGame().keySet()){
                    if(serverClientConnection.getGameHandler().getPlayersInGame().get(serverClientConnection).getDashboard().getLeaderCards().size() != 2){
                        //System.out.println("entro nell'if");
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

                scc.send(new ACKMessage("OK"));
                for (Player player : scc.getGameHandler().getPlayersInGame().values()) {
                    if (!player.getDashboard().getInkwell() && player.getDashboard().getStock().stockIsEmpty()) {
                        System.out.println("non dovrei entrare in questo if");
                        return;
                    }

                    //le risorse sono settate devo creare le view e mandarle


                    //System.out.println("ho finito il metodo per l'invio delle view");
                }
                //System.out.println("ho chiamato il metodo che inizializza le view");
                scc.getGameHandler().initializationView();
            }else{
                scc.send(new NACKMessage("KO-1"));
            }

        }else{
            scc.send(new NACKMessage("KO-2"));
        }
    }

    /**
     * method that handle every action message. It is the controller in charge of identify the action and
     * call the correct method
     * @param actionMessage contains the action made by the player and the information needed to pursue that action
     */
    public void handleMessage(ActionMessage actionMessage){
        if (scc.getGamePhase() != GamePhases.GAME){
            scc.send(new NACKMessage("Error! You are not in the correct phase of the game"));
            return;
        }

        //controllo che la richiesta mi viene fatta dal player attivo
        if(!scc.getNickname().equals(scc.getGameHandler().getGame().getActivePlayer().getNickName())){
            scc.send(new NACKMessage("Error! It's not your turn"));
            return;
        }

        if (actionMessage instanceof RequestResourcesBoughtFromMarketMessage){
            scc.send(new SendResourcesBoughtFromMarket("Risorse",scc.getGameHandler().getPlayersInGame().get(scc).getResources()));
            return;
        }

        scc.send(scc.getGameHandler().getTurnHandler().doAction(actionMessage));
    }

    /**
     * method able to notify all the player that the active player has changed because someone has ended his turn
     *
     */
    public void handleMessage(EndTurnMessage message){
        if(scc.getGamePhase() == GamePhases.GAME){
            //per ongi player mando il messaggio che è cambiato il turno
            UpdateActivePlayerMessage updateActivePlayerMessage= scc.getGameHandler().getTurnHandler().endTurn();

            for (ServerClientConnection serverClientConnection: scc.getGameHandler().getPlayersInGame().keySet()){
                serverClientConnection.send(updateActivePlayerMessage);
            }
        }else{
            scc.send(new NACKMessage("KO"));
        }
    }
}
