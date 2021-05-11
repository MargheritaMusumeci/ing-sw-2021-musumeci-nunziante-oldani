package it.polimi.ingsw.messages.updateMessages;

import java.io.Serializable;

/**
 * Message sent by server for notify that someone bought by evolution section and its view must be update
 */
public class UpdateEvolutionSectionMessage extends UpdateMessage implements Serializable {

    public UpdateEvolutionSectionMessage(String message) {
        super(message);
    }
}
