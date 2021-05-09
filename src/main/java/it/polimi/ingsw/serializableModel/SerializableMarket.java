package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.game.Resource;

import java.io.Serializable;

public class SerializableMarket implements Serializable {

    private Resource externalResource;
    private Resource[][] market;

    public SerializableMarket(Resource externalResource, Resource[][] market) {
        this.externalResource = externalResource;
        this.market = market;
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
