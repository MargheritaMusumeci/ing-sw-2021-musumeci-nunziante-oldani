package it.polimi.ingsw.messages.sentByServer.configurationMessagesServer;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

public class SendViewMessage extends ServerConfigurationMessage {

    View view;

    public SendViewMessage(String message, View view) {
        super(message);
        this.view=view;
    }

    public View getView() {
        return view;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }
}
