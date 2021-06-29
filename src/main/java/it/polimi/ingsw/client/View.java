package it.polimi.ingsw.client;

import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * class that contains alla the information of the game and the players related to the client side
 */
//TODO remove methods never used
public class View implements Serializable {

    private String activePlayer;
    private String nickname;
    private SerializableDashboard dashboard;
    private HashMap<String, SerializableDashboard> enemiesDashboard;
    private HashMap<SerializableDashboard, ArrayList<SerializableLeaderCard>> enemiesActivatedLeaderCards;
    private SerializableMarket market;
    private SerializableEvolutionSection evolutionSection;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private int score;
    private ArrayList<Resource> resourcesBoughtFromMarker;
    private ArrayList<String> winners;
    private HashMap<String, Integer> scores;

    public View(String activePlayer, String nickname, SerializableDashboard dashboard, HashMap<String, SerializableDashboard> enemiesDashboard,
                SerializableMarket market, SerializableEvolutionSection evolutionSection, ArrayList<SerializableLeaderCard> leaderCards, int score) {
        this.activePlayer=activePlayer;
        this.nickname = nickname;
        this.dashboard = dashboard;
        this.enemiesDashboard = enemiesDashboard;
        this.market = market;
        this.evolutionSection = evolutionSection;
        this.leaderCards = leaderCards;
        this.score = score;
        resourcesBoughtFromMarker = new ArrayList<>();
        enemiesActivatedLeaderCards = new HashMap<>();
        for (SerializableDashboard enemyDashboard :
                enemiesDashboard.values()) {
            enemiesActivatedLeaderCards.put(enemyDashboard, new ArrayList<>());
        }
        this.winners = new ArrayList<>();
        this.scores = new HashMap<>();
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public SerializableDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(SerializableDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public SerializableMarket getMarket() {
        return market;
    }

    public void setMarket(SerializableMarket market) {
        this.market = market;
    }

    public SerializableEvolutionSection getEvolutionSection() {
        return evolutionSection;
    }

    public void setEvolutionSection(SerializableEvolutionSection evolutionSection) {
        this.evolutionSection = evolutionSection;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public HashMap<String, SerializableDashboard> getEnemiesDashboard() {
        return enemiesDashboard;
    }

    public void setEnemiesDashboard(HashMap<String, SerializableDashboard> enemiesDashboard) {
        this.enemiesDashboard = enemiesDashboard;
    }

    public void setEnemyActivatedLeaderCards(SerializableDashboard dashboard, ArrayList<SerializableLeaderCard> enemyActivatedLeaderCards) {
        this.enemiesActivatedLeaderCards.put(dashboard, enemyActivatedLeaderCards);
    }

    public void setEnemyDashboard(SerializableDashboard dashboard, String nickname){
        enemiesDashboard.put(nickname, dashboard);
    }

    public HashMap<SerializableDashboard, ArrayList<SerializableLeaderCard>> getEnemiesActivatedLeaderCards() {
        return enemiesActivatedLeaderCards;
    }

    public void addEnemyLeaderCard(SerializableDashboard enemyDashboard, SerializableLeaderCard leaderCard){
        enemiesActivatedLeaderCards.get(enemyDashboard).add(leaderCard);
    }

    public ArrayList<Resource> getResourcesBoughtFromMarker() {
        return resourcesBoughtFromMarker;
    }

    public void setResourcesBoughtFromMarker(ArrayList<Resource> resourcesBoughtFromMarker) {
        this.resourcesBoughtFromMarker = (ArrayList<Resource>) resourcesBoughtFromMarker.clone();
    }

    public void setEnemiesActivatedLeaderCards(HashMap<SerializableDashboard, ArrayList<SerializableLeaderCard>> enemiesActivatedLeaderCards) {
        this.enemiesActivatedLeaderCards = enemiesActivatedLeaderCards;
    }

    public ArrayList<String> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<String> winners) {
        this.winners = winners;
    }

    public HashMap<String, Integer> getScores() {
        return scores;
    }

    public void setScores(HashMap<String, Integer> scores) {
        this.scores = scores;
    }
}
