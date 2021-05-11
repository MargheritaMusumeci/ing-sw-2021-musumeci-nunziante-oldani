package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Resource;

import java.io.Serializable;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the Market.
 *
 */
public class SerializableMarket implements Serializable {

    private Resource externalResource;
    private Resource[][] market;

    public SerializableMarket(Market market) {
        this.externalResource = market.getExternalResource();
        this.market = market.getMarketBoard();
    }

    public Resource getExternalResource() {
        return externalResource;
    }

    public void setExternalResource(Resource externalResource) {
        this.externalResource = externalResource;
    }

    public Resource[][] getMarket() {
        return market;
    }

    public void setMarket(Resource[][] market) {
        this.market = market;
    }
}
