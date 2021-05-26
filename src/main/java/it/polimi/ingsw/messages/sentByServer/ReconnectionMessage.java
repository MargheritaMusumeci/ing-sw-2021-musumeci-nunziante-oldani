package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.messageHandler.MessageHandler;



/**
 * Message sent by server for notify that nickname provided exist and begin the attempt to reconnect
 */
public class ReconnectionMessage extends ServerMessage {

    private View view;

    public ReconnectionMessage(String message, View view) {
        super(message);
        this.view = view;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }

    public View getView() {
        return view;
    }
}
