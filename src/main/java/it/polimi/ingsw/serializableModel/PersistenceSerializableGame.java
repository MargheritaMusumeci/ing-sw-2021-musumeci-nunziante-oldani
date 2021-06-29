package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.board.LeaderProductionZone;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.server.Persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PersistenceSerializableGame implements Serializable{

    private ArrayList<String> playerNicknames;
    private String activePlayerNickname;

    private SerializableMarket market;
    private ArrayList<EvolutionCard>[][] evolutionSection;

    private HashMap<String,PopeTrack> popeTrackHashMap;

    private HashMap<String,SerializableDashboard> playerDashboards;

    private HashMap<String, Integer> playerPositions;

    private HashMap<String, ArrayList<LeaderCard>> leaderCards;

    private HashMap<String, ArrayList<LeaderProductionZone>> leaderProductionZoneHashMap;


    public PersistenceSerializableGame(Game game){
        activePlayerNickname = game.getActivePlayer().getNickName();

        System.out.println("Persistance active player nickname: "+activePlayerNickname);
        playerNicknames = new ArrayList<>();
        playerDashboards = new HashMap<>();
        playerPositions = new HashMap<>();
        leaderCards = new HashMap<>();
        popeTrackHashMap = new HashMap<>();
        leaderProductionZoneHashMap = new HashMap<>();

        for(Player player: game.getPlayers()){
            playerNicknames.add(player.getNickName());
            playerDashboards.put(player.getNickName(), new SerializableDashboard(player.getDashboard()));
            playerPositions.put(player.getNickName(), player.getPopeTrack().getGamerPosition().getIndex());

            leaderCards.put(player.getNickName(),player.getDashboard().getLeaderCards());
            popeTrackHashMap.put(player.getNickName(),player.getPopeTrack());
            leaderProductionZoneHashMap.put(player.getNickName(),player.getDashboard().getLeaderProductionZones());
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

    public HashMap<String, PopeTrack> getPopeTrackHashMap() {
        return popeTrackHashMap;
    }

    public HashMap<String, ArrayList<LeaderCard>> getLeaderCards() {
        return leaderCards;
    }

    public HashMap<String, ArrayList<LeaderProductionZone>> getLeaderProductionZoneHashMap() {
        return leaderProductionZoneHashMap;
    }
}
