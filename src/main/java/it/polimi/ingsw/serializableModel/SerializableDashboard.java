package it.polimi.ingsw.serializableModel;

import java.io.Serializable;

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

    public SerializableDashboard(SerializableLockBox serializableLockBox, SerializableStock serializableStock,
                                 SerializablePopeTack serializablePopeTack, SerializableProductionZone[] serializableProductionZones,
                                 String nickname , boolean inkwell) {
        this.serializableLockBox = serializableLockBox;
        this.serializableStock = serializableStock;
        this.serializablePopeTack = serializablePopeTack;
        this.serializableProductionZones = serializableProductionZones;
        this.nickname = nickname;
        this.inkwell = inkwell;
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

