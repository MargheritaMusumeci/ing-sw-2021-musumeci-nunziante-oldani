package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.server.virtualView.VirtualView;

import java.io.Serializable;

/**
 * Message sent by server to inform no-active player that their view must be update because active player has done an action.
 */
public class UpdateOtherPlayerView extends UpdateMessage implements Serializable {

    private VirtualView virtualView;
    private String nickname;

    public UpdateOtherPlayerView(String message, VirtualView virtualView, String nickname) {
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
}
