package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.serializableModel.SerializableDashboard;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;

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
}
