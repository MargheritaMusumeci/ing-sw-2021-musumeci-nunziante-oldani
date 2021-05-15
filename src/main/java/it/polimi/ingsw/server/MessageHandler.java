package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.actionMessages.ActionMessage;
import it.polimi.ingsw.messages.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.messages.actionMessages.SendResourcesBoughtFromMarket;
import it.polimi.ingsw.messages.configurationMessages.*;
import it.polimi.ingsw.messages.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.model.players.Player;

public class MessageHandler {

    private Server server;

    public MessageHandler(Server server){
        this.server = server;
    }

    public void handleMessage(Message message, ServerClientConnection scc){

        if(message instanceof PingMessage){
            scc.getPingSender().pingRecived();
        }
        if(message instanceof ConfigurationMessage){
            handleConfigurationMessages((ConfigurationMessage) message, scc);
        }

        if(message instanceof ActionMessage){
            handleActionMessages((ActionMessage) message, scc);
        }

        if(message instanceof EndTurnMessage){
            //per ongi player mando il messaggio che è cambiato il turno
            UpdateActivePlayerMessage updateActivePlayerMessage= scc.getGameHandler().getTurnHandler().endTurn();

            for (ServerClientConnection serverClientConnection: scc.getGameHandler().getPlayersInGame().keySet()){
                serverClientConnection.send(updateActivePlayerMessage);
            }
        }

    }


    private void handleConfigurationMessages(ConfigurationMessage message, ServerClientConnection scc){

        if(scc.getGamePhase() == GamePhases.CONFIGURATION){

            if(message instanceof NickNameMessage){
                NickNameMessage nmessage = (NickNameMessage) message;
                //controllo che il giocatore non sia gia un giocatore di quelli che attendevano di riconnettersi
                if(server.checkDisconnectedPlayer(nmessage.getMessage()) != null){
                    //gestisco la riconnessione
                    ServerClientConnection scc_temp = server.checkDisconnectedPlayer(nmessage.getMessage());
                    scc_temp.reconnect(scc.getSocket());
                    //dovrei elimanre dalla hashmap questa virtual view
                    server.removeWaitForReconnection(scc_temp);
                    //send reconnection Message
                    scc_temp.send(new ReconnectionMessage("Re-entering the game"));
                    return;
                }
                if(!server.checkNickname(nmessage.getMessage())){
                    scc.send(new NACKMessage("Error! This nickname has already been taken"));
                } else {
                    scc.send(new ACKMessage("OK"));
                    scc.setNickname(nmessage.getMessage());
                    server.addTakenNickname(nmessage.getMessage());
                }
                return;
            }

            if(message instanceof NumberOfPlayerMessage){
                NumberOfPlayerMessage numMessage = (NumberOfPlayerMessage) message;
                //devo capire in che fase si trova il nostro player
                if(scc.getNickname() == null){
                    scc.send( new NACKMessage("Error! You have not chose your nickname yet"));
                }else{
                    switch (Integer.parseInt(numMessage.getMessage())){
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
                    return;
                }

            }
        } else if(scc.getGamePhase() == GamePhases.INITIALIZATION){

            if(message instanceof LeaderCardChoiceMessage){
                if(scc.getGameHandler().getInitializationHandler().setLeaderCards( scc.getGameHandler().getPlayersInGame().get(scc),
                        ((LeaderCardChoiceMessage) message).getLeaderCards())){
                    scc.send(new ACKMessage("OK"));
                    //controllare che tutti i giocatori del gamehandler abbiamo un arraylist di leaderCaard con size 2
                    for(ServerClientConnection serverClientConnection : scc.getGameHandler().getPlayersInGame().keySet()){
                        if(serverClientConnection.getGameHandler().getPlayersInGame().get(serverClientConnection).getDashboard().getLeaderCards().size() != 2){
                            //System.out.println("entro nell'if");
                            return;
                        }
                    }
                    scc.getGameHandler().handleInitialResourcesSettings();
                }

                else
                    scc.send(new NACKMessage("KO"));
            }

            if(message instanceof SelectedInitialResourceMessage) {
                if (scc.getGameHandler().getInitializationHandler().setInitialResources(scc.getGameHandler().getPlayersInGame().get(scc),
                        ((SelectedInitialResourceMessage) message).getResources())) {
                    scc.send(new ACKMessage("OK"));
                    for (Player player : scc.getGameHandler().getPlayersInGame().values()) {
                        if (!player.getDashboard().getInkwell() && player.getDashboard().getStock().stockIsEmpty()) {
                            //System.out.println("non dovrei entrare in questo if");
                            return;
                        }

                        //le risorse sono settate devo creare le view e mandarle
                        //System.out.println("ho chiamato il metodo che inizializza le view");

                        //System.out.println("ho finito il metodo per l'invio delle view");
                    }
                    scc.getGameHandler().initializationView();
                }
                else
                    scc.send(new NACKMessage("KO"));
            }

        }else {
            scc.send(new NACKMessage("Error! You are not in the correct phase of the game"));
        }

    }

    private void handleActionMessages(ActionMessage actionMessage, ServerClientConnection scc){
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
            scc.send(new SendResourcesBoughtFromMarket("",scc.getGameHandler().getPlayersInGame().get(scc).getResources()));
            return;
        }

        scc.send(scc.getGameHandler().getTurnHandler().doAction(actionMessage));
    }
}
