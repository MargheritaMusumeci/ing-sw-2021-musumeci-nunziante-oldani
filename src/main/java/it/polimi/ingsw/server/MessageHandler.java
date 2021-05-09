package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.NACKMessage;
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
        if(message instanceof ConfigurationMessage){
            handleConfigurationMessages((ConfigurationMessage) message, scc);
        }
    }

    private void handleConfigurationMessages(ConfigurationMessage message, ServerClientConnection scc){

        if(scc.getGamePhase() == GamePhases.CONFIGURATION){

            if(message instanceof NickNameMessage){
                NickNameMessage nmessage = (NickNameMessage) message;
                //controllo che il giocatore non sia gia un giocatore di quelli che attendevano di riconnettersi
                if(server.checkDisconnectedPlayer(nmessage.getMessage()) != null){
                    //gestisco la riconnessione
                    //dovrei elimanre dalla hashmap questa virtual view
                    //send reconnection Message
                    return;
                }
                if(!server.checkNickname(nmessage.getMessage())){
                    scc.send(new NACKMessage("Error! This nickname has already been taken"));
                } else {
                    scc.setNickname(nmessage.getMessage());
                    scc.send(new ACKMessage("OK"));
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

            }

        }else {
            scc.send(new NACKMessage("Error! You are not in the correct phase of the game"));
        }






    }
}
