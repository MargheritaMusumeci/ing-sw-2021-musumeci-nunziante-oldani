package it.polimi.ingsw.messages.sentByServer;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

/**
 * Message sent by server for notify that nickname provided exist and begin the attempt to reconnect
 */
public class ReconnectionMessage extends ServerMessage {

    private View view;
    private int numberOfPlayers;

    public ReconnectionMessage(String message, View view, int numberOfPlayers) {
        super(message);
        this.view = view;
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }

    public View getView() {
        return view;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
