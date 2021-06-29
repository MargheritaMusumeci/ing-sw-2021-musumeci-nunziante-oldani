package it.polimi.ingsw.messages.sentByServer.updateMessages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.messageHandler.MessageHandler;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.server.virtualView.VirtualView;

import java.io.Serializable;

/**
 * Message sent by server to inform no-active player that their view must be update because active player has done an action.
 */
public class UpdateOtherPlayerViewMessage extends UpdateMessage implements Serializable {

    private View view;
    private String nickname;

    public UpdateOtherPlayerViewMessage(String message, View view, String nickname) {
        super(message);
        this.nickname = nickname;
        this.view = view;

        //remove the leader cards that i cannot see
        for(int i=0; i<view.getLeaderCards().size(); i++){
            if(!view.getLeaderCards().get(i).isActive()){
                view.getLeaderCards().remove(i);
                i--;
            }
        }

    }

    public String getNickname() {
        return nickname;
    }

    public View getView() {
        return view;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }
}
