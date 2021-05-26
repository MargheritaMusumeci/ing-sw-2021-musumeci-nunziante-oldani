package it.polimi.ingsw.client;

public enum GameFxml {

    IP_PORT("ip_port_configuration.fxml"),
    NICKNAME("nickname_configuration.fxml"),
    PLAYERS ("players_configuration.fxml"),
    LEADER_CARD("leader_cards_configuration.fxml"),
    INITIAL_RESOURCES("initial_resources_configuration.fxml"),
    WAITING_ROOM("waiting.fxml"),
    START_GAME("view.fxml"),
    MARKET("market.fxml"),
    STORE_RESOURCES("store_resources.fxml"),
    BASIC_PRODUCTION("basic_production.fxml"),
    LEADER_PRODUCTION("leader_production.fxml"),
    EVOLUTION_SECTION("evolution_section.fxml"),
    PRODUCTION_ZONE_CHOICE("production_zone_choice.fxml"),
    ENDGAME("end_game.fxml"),
    OTHERVIEW("enemyView.fxml");

    private GamePhases gamePhases;
    public final String s;

    static{
        IP_PORT.gamePhases=GamePhases.IINITIALIZATION;
        NICKNAME.gamePhases=GamePhases.NICKNAME;
        PLAYERS.gamePhases=GamePhases.NUMBEROFPLAYERS;
        LEADER_CARD.gamePhases=GamePhases.INITIALLEADERCARDSELECTION;
        INITIAL_RESOURCES.gamePhases=GamePhases.INITIALRESOURCESELECTION;
        WAITING_ROOM.gamePhases=GamePhases.WAITINGOTHERPLAYERS;
        START_GAME.gamePhases=GamePhases.STARTGAME;
        MARKET.gamePhases=GamePhases.BUYFROMMARKET;
        STORE_RESOURCES.gamePhases=GamePhases.STORERESOURCES;
        BASIC_PRODUCTION.gamePhases=GamePhases.ASKACTIVEPRODUCTION;
        LEADER_PRODUCTION.gamePhases=GamePhases.LEADERPRODUCTION;
        EVOLUTION_SECTION.gamePhases=GamePhases.BUYEVOLUTIONCARD;
        PRODUCTION_ZONE_CHOICE.gamePhases=GamePhases.PRODUCTIONZONECHOICE;
        ENDGAME.gamePhases=GamePhases.ENDGAME;
        OTHERVIEW.gamePhases=GamePhases.SEEOTHERVIEW;
    }

    public GamePhases getGamePhases(){
        return this.gamePhases;
    }

    GameFxml(String s) {
        this.s=s;
    }

}
