package it.polimi.ingsw.client.gui;

/**
 * Enum that contains constant strings representing fxml files. The corresponding game phase is also associated with each file.
 */
public enum GameFxml {

    IP_PORT("ip_port_configuration.fxml"),
    NICKNAME("nickname_configuration.fxml"),
    PLAYERS ("players_configuration.fxml"),
    LEADER_CARD("leader_cards_configuration.fxml"),
    INITIAL_RESOURCES("initial_resources_configuration.fxml"),
    WAITING_ROOM("waiting.fxml"),
    MY_TURN("newView.fxml"),
    MARKET("market.fxml"),
    STORE_RESOURCES("store_resources.fxml"),
    BASIC_PRODUCTION("basic_production.fxml"),
    LEADER_PRODUCTION("leader_production.fxml"),
    EVOLUTION_SECTION("evolution_section.fxml"),
    PRODUCTION_ZONE_CHOICE("production_zone_choice.fxml"),
    ENDGAME("end_game.fxml"),
    OTHER_TURN("newView.fxml"),
    CHOOSE_WHITE_RESOURCES("white_ball_choose.fxml"),
    OTHER_VIEW("enemyView.fxml");

    private GamePhases gamePhases;
    public final String s;

    static{
        IP_PORT.gamePhases=GamePhases.INITIALIZATION;
        NICKNAME.gamePhases=GamePhases.NICKNAME;
        PLAYERS.gamePhases=GamePhases.NUMBEROFPLAYERS;
        LEADER_CARD.gamePhases=GamePhases.INITIALLEADERCARDSELECTION;
        INITIAL_RESOURCES.gamePhases=GamePhases.INITIALRESOURCESELECTION;
        WAITING_ROOM.gamePhases=GamePhases.WAITINGOTHERPLAYERS;
        MY_TURN.gamePhases=GamePhases.MYTURN;
        MARKET.gamePhases=GamePhases.BUYFROMMARKET;
        STORE_RESOURCES.gamePhases=GamePhases.STORERESOURCES;
        BASIC_PRODUCTION.gamePhases=GamePhases.ASKACTIVEPRODUCTION;
        LEADER_PRODUCTION.gamePhases=GamePhases.LEADERPRODUCTION;
        EVOLUTION_SECTION.gamePhases=GamePhases.BUYEVOLUTIONCARD;
        PRODUCTION_ZONE_CHOICE.gamePhases=GamePhases.PRODUCTIONZONECHOICE;
        ENDGAME.gamePhases=GamePhases.ENDGAME;
        OTHER_VIEW.gamePhases=GamePhases.SEEOTHERVIEW;
        CHOOSE_WHITE_RESOURCES.gamePhases=GamePhases.CHOOSEWHITEBALL;
        OTHER_TURN.gamePhases = GamePhases.OTHERPLAYERSTURN;
    }

    public GamePhases getGamePhases(){
        return this.gamePhases;
    }

    GameFxml(String s) {
        this.s=s;
    }

}
