package it.polimi.ingsw.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage(){ return message;}
}
