package it.polimi.ingsw.messages.configurationMessages;

import it.polimi.ingsw.client.View;

public class SendViewMessage extends ConfigurationMessage{

    View view;

    public SendViewMessage(String message, View view) {
        super(message);
        this.view=view;
    }

    public View getView() {
        return view;
    }
}
