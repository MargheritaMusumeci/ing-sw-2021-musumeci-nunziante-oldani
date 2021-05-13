package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.serializableModel.SerializableMarket;

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
}
