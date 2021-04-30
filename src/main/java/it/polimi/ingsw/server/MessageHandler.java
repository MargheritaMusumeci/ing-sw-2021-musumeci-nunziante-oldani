package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
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
        if(message instanceof NickNameMessage){

        }

        if(message instanceof LeaderCardChoiceMessage){

        }

        if(message instanceof NumberOfPlayerMessage){

        }
    }
}
