package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUI extends Application implements UI {

    //list of file .fxml
    public static final String IP_PORT = "ip_port_configuration.fxml";
    public static final String NICKNAME = "nickname_configuration.fxml";
    public static final String PLAYERS = "players_configuration.fxml";
    public static final String LEADER_CARD = "leader_cards_configuration.fxml";
    public static final String INITIAL_RESOURCES = "initial_resources_configuration.fxml";
    public static final String WAITING_ROOM = "waiting.fxml";
    public static final String START_GAME = "view.fxml";
    public static final String MARKET = "market.fxml";
    public static final String STORE_RESOURCES = "store_resources.fxml";
    public static final String BASIC_PRODUCTION = "basic_production.fxml";
    public static final String LEADER_PRODUCTION = "leader_production.fxml";
    public static final String EVOLUTION_SECTION = "evolution_section.fxml";
    public static final String PRODUCTION_ZONE_CHOICE = "production_zone_choice.fxml";
    public static final String ENDGAME = "end_game.fxml";

    private Scene currentScene;
    private Scene oldScene;
    private Stage currentStage;
    private final HashMap<String, Scene> scenes;
    private final HashMap<String, Controller> controllers;
    private final HashMap<Scene, GamePhases> phases;
    private final HashMap<GamePhases,String> fxmls;

    private View view;
    private ClientSocket clientSocket;
    private Socket socket;
    private GamePhases gamePhase;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private ArrayList<Resource> resources;
    private String nickname;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String errorFromServer;
    private int players;
    private boolean actionDone;

    /**
     * To set which evolution card the player wants to buy
     */
    private int cardRow;
    private int cardColumn;

    private ArrayList<Resource> basicRequires;
    private ArrayList<Resource> basicEnsures;
    private HashMap<Integer,Resource> leaderEnsure;
    private Integer leaderPosition;

    public GUI() {
        gamePhase = GamePhases.IINITIALIZATION;
        scenes = new HashMap<>();
        controllers = new HashMap<>();
        phases = new HashMap<>();
        fxmls = new HashMap<>();
        errorFromServer="";
        leaderEnsure = new HashMap<>();
        players=0;
    }

    public static void main(String[] args) {
        new GUI();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.currentStage = primaryStage;
        initializationFXMLParameter();
        initializationStage();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public void initializationFXMLParameter() {
        List<GameFxml> fxmlFiles = new ArrayList<>(Arrays.asList(GameFxml.values()));
        try {
            for (GameFxml path : fxmlFiles) {
                URL url = new File("src/main/resources/fxml/" + path.s).toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Scene scene = new Scene(loader.load());
                scenes.put(path.s, scene);
                Controller controller = loader.getController();
                controller.setGui(this);
                controllers.put(path.s, controller);
                phases.put(scene,path.getGamePhases());
                fxmls.put(path.getGamePhases(),path.s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = scenes.get(IP_PORT);
    }

    public void initializationStage() {
        currentStage.setTitle("Masters of Renaissance");
        currentStage.setScene(currentScene);
        currentStage.show();
    }

    public void changeScene() {

        Platform.runLater(()->{
            System.out.println("Phase is: " + gamePhase);
            //ack e nack inutili
            isAckArrived=false;
            isNackArrived=false;
            currentStage.setScene(currentScene);
            controllers.get(fxmls.get(gamePhase)).init();
            System.out.println("show scene " + phases.get(currentScene));
            currentStage.show();
            errorFromServer="";
        });
    }

    //GETTER AND SETTER
    public Scene getOldScene(){
        return oldScene;
    }

    public void setOldScene(Scene scene) {
        oldScene=scene;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public GamePhases phase(Scene scene) {
        return phases.get(scene);
    }

    public String getErrorFromServer() {
        return errorFromServer;
    }

    public void setErrorFromServer(String errorFromServer) {
        this.errorFromServer = errorFromServer;
    }

    public boolean isAckArrived() {
        return isAckArrived;
    }

    public void setAckArrived(boolean ackArrived) {
        isAckArrived = ackArrived;
    }

    public boolean isNackArrived() {
        return isNackArrived;
    }

    public void setNackArrived(boolean nackArrived) {
        isNackArrived = nackArrived;
    }

    public void setSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
    }

    public void setClientSocket() {
        try {
            clientSocket = new ClientSocket(this, socket);
        } catch (IOException e) {
            clientSocket = null;
        }
    }

    public void setGamePhase(GamePhases phase) {
        gamePhase = phase;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Scene getScene(String NAME) {
        return scenes.get(NAME);
    }

    public Controller getController(String NAME) {
        return controllers.get(NAME);
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public GamePhases getGamePhase() {
        return gamePhase;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public static String getIpPort() {
        return IP_PORT;
    }

    public ArrayList<Resource> getBasicRequires() {
        return basicRequires;
    }

    public void setBasicRequires(ArrayList<Resource> basicRequires) {
        this.basicRequires = basicRequires;
    }

    public ArrayList<Resource> getBasicEnsures() {
        return basicEnsures;
    }

    public void setBasicEnsures(ArrayList<Resource> basicEnsures) {
        this.basicEnsures = basicEnsures;
    }

    public Integer getLeaderPosition() {
        return leaderPosition;
    }

    public void setLeaderPosition(Integer leaderPosition) {
        this.leaderPosition = leaderPosition;
    }

    public HashMap<Integer,Resource> getLeaderEnsure() {
        return leaderEnsure;
    }

    public void setLeaderEnsure(HashMap<Integer,Resource> leaderEnsure) {
        this.leaderEnsure = leaderEnsure;
    }

    public int getCardRow() { return cardRow; }

    public void setCardRow(int cardRow) { this.cardRow = cardRow; }

    public int getCardColumn() { return cardColumn; }

    public void setCardColumn(int cardColumn) { this.cardColumn = cardColumn; }

    public boolean isActionDone() {
        return actionDone;
    }

    public void setActionDone(boolean actionDone) {
        this.actionDone = actionDone;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

}