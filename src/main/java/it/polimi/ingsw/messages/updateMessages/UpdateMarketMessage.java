package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.model.game.Market;

import java.io.Serializable;

/**
 * Message sent by server for notify that someone bought by market and its view must be update
 */
public class UpdateMarketMessage extends UpdateMessage implements Serializable {
    private Market market;
    public UpdateMarketMessage(String message, Market market) {
        super(message);
        this.market = market;
    }

    public Market getMarket() {
        return market;
    }
}
