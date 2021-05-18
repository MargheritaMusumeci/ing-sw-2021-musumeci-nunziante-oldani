package it.polimi.ingsw.messages.sentByServer.updateMessages;

import it.polimi.ingsw.serializableModel.SerializableMarket;
import it.polimi.ingsw.client.messageHandler.MessageHandler;

import java.io.Serializable;

/**
 * Message sent by server for notify that someone bought by market and its view must be update
 */
public class UpdateMarketMessage extends UpdateMessage implements Serializable {
    private SerializableMarket market;
    public UpdateMarketMessage(String message, SerializableMarket market) {
        super(message);
        this.market = market;
    }

    public SerializableMarket getMarket() {
        return market;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleUpdateMessage(this);
    }
}
