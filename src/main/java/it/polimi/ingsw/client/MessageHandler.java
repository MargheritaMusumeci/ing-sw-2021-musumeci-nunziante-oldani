package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.actionMessages.SendResourcesBoughtFromMarket;
import it.polimi.ingsw.messages.configurationMessages.FourLeaderCardsMessage;
import it.polimi.ingsw.messages.configurationMessages.InitialResourcesMessage;
import it.polimi.ingsw.messages.configurationMessages.SendViewMessage;
import it.polimi.ingsw.messages.configurationMessages.StartGameMessage;
import it.polimi.ingsw.messages.updateMessages.*;

public abstract class MessageHandler {

    protected ClientSocket clientSocket;

    public abstract void handleMessage(Message message);
}

