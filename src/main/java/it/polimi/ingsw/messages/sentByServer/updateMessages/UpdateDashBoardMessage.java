package it.polimi.ingsw.messages.sentByServer.updateMessages;

import it.polimi.ingsw.serializableModel.SerializableDashboard;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

import java.io.Serializable;

/**
 * Message sent by server for notify that dashboard must be update after and action has been completed
 */
public class UpdateDashBoardMessage extends UpdateMessage implements Serializable {
    private SerializableDashboard dashboard;
    public UpdateDashBoardMessage(String message, SerializableDashboard dashboard) {
        super(message);
        this.dashboard = dashboard;
    }

    public SerializableDashboard getDashboard(){return dashboard; }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }
}
