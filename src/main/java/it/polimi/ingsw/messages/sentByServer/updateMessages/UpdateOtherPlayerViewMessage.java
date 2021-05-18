package it.polimi.ingsw.messages.sentByServer.updateMessages;

import it.polimi.ingsw.client.messageHandler.MessageHandler;
import it.polimi.ingsw.server.virtualView.VirtualView;

import java.io.Serializable;

/**
 * Message sent by server to inform no-active player that their view must be update because active player has done an action.
 */
public class UpdateOtherPlayerViewMessage extends UpdateMessage implements Serializable {

    private VirtualView virtualView;
    private String nickname;

    public UpdateOtherPlayerViewMessage(String message, VirtualView virtualView, String nickname) {
        super(message);
        this.nickname = nickname;
        this.virtualView = virtualView;
    }

    public String getNickname() {
        return nickname;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    @Override
    public void handle(MessageHandler messageHandler) {

    }
}
