package it.polimi.ingsw.messages;

import it.polimi.ingsw.messages.sentByServer.PersistenceMessage;
import it.polimi.ingsw.server.Persistence;

import java.io.Serializable;

public class PersistenceMessageToSave implements Serializable {

    private String nickname;
    private Message message;

    public PersistenceMessageToSave(String nickname, Message message){
        this.nickname = nickname;
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public Message getMessage() {
        return message;
    }
}
