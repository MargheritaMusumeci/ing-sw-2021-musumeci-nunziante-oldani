package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.model.board.Dashboard;

import java.io.Serializable;

public class UpdateDashBoardMessage extends UpdateMessage implements Serializable {
    private Dashboard dashboard;
    public UpdateDashBoardMessage(String message, Dashboard dashboard) {
        super(message);
        this.dashboard = dashboard;
    }

    public Dashboard getDashboard(){return dashboard; }
}
