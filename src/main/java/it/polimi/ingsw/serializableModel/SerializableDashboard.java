package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.board.NormalProductionZone;

import java.io.Serializable;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the Dashboard.
 *
 */
public class SerializableDashboard implements Serializable {
    private SerializableLockBox serializableLockBox;
    private SerializableStock serializableStock;
    private SerializablePopeTack serializablePopeTack;
    private SerializableProductionZone[] serializableProductionZones;
    private String nickname;
    private boolean inkwell;

    public boolean isInkwell() {
        return inkwell;
    }

    public void setInkwell(boolean inkwell) {
        this.inkwell = inkwell;
    }

    public SerializableDashboard(Dashboard dashboard) {
        this.serializableLockBox = new SerializableLockBox(dashboard.getLockBox());
        this.serializableStock = new SerializableStock(dashboard.getStock());
        this.serializablePopeTack = new SerializablePopeTack(dashboard.getPopeTrack());

        int i = 0;
        this.serializableProductionZones = new SerializableProductionZone[dashboard.getProductionZone().length];

        for (NormalProductionZone normalPZ:dashboard.getProductionZone()) {
            SerializableProductionZone serializableProductionZone = new SerializableProductionZone(normalPZ);
            this.serializableProductionZones[i]=serializableProductionZone;
            i++;
        }

        this.nickname = dashboard.getNickName();
        this.inkwell = dashboard.getInkwell();
    }

    public SerializableLockBox getSerializableLockBox() {
        return serializableLockBox;
    }

    public void setSerializableLockBox(SerializableLockBox serializableLockBox) {
        this.serializableLockBox = serializableLockBox;
    }

    public SerializableStock getSerializableStock() {
        return serializableStock;
    }

    public void setSerializableStock(SerializableStock serializableStock) {
        this.serializableStock = serializableStock;
    }

    public SerializablePopeTack getSerializablePopeTack() {
        return serializablePopeTack;
    }

    public void setSerializablePopeTack(SerializablePopeTack serializablePopeTack) {
        this.serializablePopeTack = serializablePopeTack;
    }

    public SerializableProductionZone[] getSerializableProductionZones() {
        return serializableProductionZones;
    }

    public void setSerializableProductionZones(SerializableProductionZone[] serializableProductionZones) {
        this.serializableProductionZones = serializableProductionZones;
    }

    public String getNickname() {
        return nickname;
    }


}

