package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.server.Persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PersistenceSerializableGame implements Serializable{

    private ArrayList<String> playerNicknames;
    private String activePlayerNickname;

    private SerializableMarket market;
    private ArrayList<EvolutionCard>[][] evolutionSection;

    private HashMap<String,SerializableDashboard> playerDashboards;
    private HashMap<String, Integer> playerPositions;
    private HashMap<String,Boolean[]> playerActivatedPopeCards;

    private HashMap<String, ArrayList<Integer>> playerLeaderCards;
    private HashMap<String, ArrayList<Boolean>> leaderCardsStatus; //active or not

    public PersistenceSerializableGame(Game game){
        activePlayerNickname = game.getActivePlayer().getNickName();
        System.out.println("Persistance active player nickname: "+activePlayerNickname);
        playerNicknames = new ArrayList<>();
        playerDashboards = new HashMap<>();
        playerPositions = new HashMap<>();
        playerActivatedPopeCards = new HashMap<>();
        playerLeaderCards = new HashMap<>();
        leaderCardsStatus = new HashMap<>();

        for(Player player: game.getPlayers()){
            playerNicknames.add(player.getNickName());
            playerDashboards.put(player.getNickName(), new SerializableDashboard(player.getDashboard()));
            playerPositions.put(player.getNickName(), player.getPopeTrack().getGamerPosition().getIndex());
            playerLeaderCards.put(player.getNickName(), new ArrayList<>());
            leaderCardsStatus.put(player.getNickName(), new ArrayList<>());
            for (LeaderCard leaderCard : player.getDashboard().getLeaderCards()){
                playerLeaderCards.get(player.getNickName()).add(leaderCard.getId());
                leaderCardsStatus.get(player.getNickName()).add(leaderCard.isActive());
            }
        }

        market = new SerializableMarket(game.getMarket());
        evolutionSection = game.getEvolutionSection().getEvolutionSection();

        //mancano solo le pope cards ma quelle poi le facciamo easyyyy

    }

    public ArrayList<String> getPlayerNicknames() {
        return playerNicknames;
    }

    public String getActivePlayerNickname() {
        return activePlayerNickname;
    }

    public SerializableMarket getMarket() {
        return market;
    }

    public ArrayList<EvolutionCard>[][] getEvolutionSection() {
        return evolutionSection;
    }

    public HashMap<String, SerializableDashboard> getPlayerDashboards() {
        return playerDashboards;
    }

    public HashMap<String, Integer> getPlayerPositions() {
        return playerPositions;
    }

    public HashMap<String, Boolean[]> getPlayerActivatedPopeCards() {
        return playerActivatedPopeCards;
    }

    public HashMap<String, ArrayList<Integer>> getPlayerLeaderCards() {
        return playerLeaderCards;
    }

    public HashMap<String, ArrayList<Boolean>> getLeaderCardsStatus() {
        return leaderCardsStatus;
    }
}
