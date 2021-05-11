package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.players.HumanPlayer;

import java.io.Serializable;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the EvolutionSection.
 * The humanPlayer is required to build this class because in that class there is the method for knowing the cards
 * the player actually buy.
 *
 */
public class SerializableEvolutionSection implements Serializable {

    private EvolutionCard[][] evolutionCards;
    private boolean[][] canBuyEvolutionCards;

    public SerializableEvolutionSection(EvolutionSection evolutionSection, HumanPlayer humanPlayer) {
        this.evolutionCards = evolutionSection.canBuy();
        this.canBuyEvolutionCards = humanPlayer.getPossibleEvolutionCard();
    }

    public EvolutionCard[][] getEvolutionCards() {
        return evolutionCards;
    }

    public void setEvolutionCards(EvolutionCard[][] evolutionCards) {
        this.evolutionCards = evolutionCards;
    }

    public boolean[][] getCanBuyEvolutionCards() {
        return canBuyEvolutionCards;
    }

    public void setCanBuyEvolutionCards(boolean[][] canBuyEvolutionCards) {
        this.canBuyEvolutionCards = canBuyEvolutionCards;
    }
}
