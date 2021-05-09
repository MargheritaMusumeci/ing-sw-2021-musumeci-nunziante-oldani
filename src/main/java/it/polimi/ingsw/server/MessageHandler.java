package it.polimi.ingsw.server;

import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.actionMessages.ActionMessage;
import it.polimi.ingsw.messages.configurationMessages.ConfigurationMessage;
import it.polimi.ingsw.messages.configurationMessages.LeaderCardChoiceMessage;
import it.polimi.ingsw.messages.configurationMessages.NickNameMessage;
import it.polimi.ingsw.messages.configurationMessages.NumberOfPlayerMessage;

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
            if(scc.getGamePhase() != GamePhases.GAME){
                scc.send(new NACKMessage("Error! You are not in the correct phase of the game"));
                return;
            }
            try {
                if(scc.getGameHandler().getTurnHandler().doAction(message)){
                    //ho gia mandato i messaggi di update
                    scc.send(new ACKMessage("OK"));
                }else{
                    scc.send(new NACKMessage("KO"));
                }
            } catch (ExcessOfPositionException e) {
                //useless exception --> to be removed
            }
        }

        if(message instanceof EndTurnMessage){
            scc.getGameHandler().getTurnHandler().endTurn();
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
                scc.getGameHandler().getInitializationHandler().setLeaderCards( scc.getGameHandler().getPlayersInGame().get(scc),
                        ((LeaderCardChoiceMessage) message).getLeaderCards() );
            }

        }else {
            scc.send(new NACKMessage("Error! You are not in the correct phase of the game"));
        }






    }
}
