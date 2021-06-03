package it.polimi.ingsw.messages.sentByServer.configurationMessagesServer;

import it.polimi.ingsw.client.messageHandler.MessageHandler;
import it.polimi.ingsw.messages.sentByServer.ServerMessage;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import it.polimi.ingsw.serializableModel.SerializableMarket;

public class MarketAndEvolutionSectionMessage extends ServerConfigurationMessage {
    SerializableMarket serializableMarket;
    SerializableEvolutionSection serializableEvolutionSection;
    public MarketAndEvolutionSectionMessage(String message, SerializableMarket serializableMarket, SerializableEvolutionSection serializableEvolutionSection) {
        super(message);
        this.serializableMarket = serializableMarket;
        this.serializableEvolutionSection = serializableEvolutionSection;
    }

    @Override
    public void handle(MessageHandler messageHandler) {
        messageHandler.handleMessage(this);
    }

    public SerializableMarket getSerializableMarket() {
        return serializableMarket;
    }

    public SerializableEvolutionSection getSerializableEvolutionSection() {
        return serializableEvolutionSection;
    }
}
