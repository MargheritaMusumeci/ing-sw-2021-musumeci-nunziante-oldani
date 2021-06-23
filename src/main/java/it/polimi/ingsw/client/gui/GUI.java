package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.client.gui.controllers.ViewEnemyController;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.serializableModel.SerializableMarket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Main class of the gui.
 * It takes care of initializing the game, changing the scene according to the phase of the game and contains common information between controllers
 */
public class GUI extends Application {

    private Scene currentScene;
    private Scene oldScene;
    private Stage currentStage;
    /**
     * Hashmap that contains link between constant name related to fxml file and corresponding scene
     */
    private final HashMap<String, Scene> scenes;
    /**
     * Hashmap that contains link between constant name related to fxml file and corresponding controller
     */
    private final HashMap<String, Controller> controllers;
    /**
     * Hashmap that contains link between scene and corresponding phase
     */
    private final HashMap<Scene, GamePhases> phases;
    /**
     * Hashmap that contains link between game phase and corresponding constant name related to fxml file
     */
    private final HashMap<GamePhases,String> fxml;
    /**
     * Copy of market necessary in the configuration phases
     */
    private SerializableMarket market;
    /**
     * Copy of market evolution section necessary in the configuration phases
     */
    private SerializableEvolutionSection evolutionSection;
    private View view;
    private ClientSocket clientSocket;
    private Socket socket;
    /**
     * Current game Phase
     */
    private GamePhases gamePhase;
    private ArrayList<SerializableLeaderCard> leaderCards;
    protected ArrayList<Integer> stockLeaderCardInUse;

    /**
     * This array list contains the position of the leader cards discarded
     */
    private ArrayList<Boolean> leaderCardsDiscarded;
    private ArrayList<Resource> resources;
    private String nickname;
    /**
     * Variable in which will be stored error received from server
     */
    private String errorFromServer;
    private int players;
    private boolean actionDone;
    /**
     * Nickname of the player of which I want to see the view
     */
    private String otherView;

    /**
     * To avoid problem with ack/update message
     */
    private boolean isAckArrived;
    private boolean isNackArrived;
    private boolean isUpdateDashboardArrived;

    /**
     * To set which evolution card the player wants to buy
     */
    private int cardRow;
    private int cardColumn;

    /**
     * Resources to be converted from basic production
     */
    private ArrayList<Resource> basicRequires;
    /**
     * Resources to be obtained from basic production
     */
    private ArrayList<Resource> basicEnsures;
    /**
     * Resources to be obtained from leader production
     */
    private HashMap<Integer,Resource> leaderEnsure;

    /**
     * Leader card the wait for ack or
     * The player is selecting the production of the leader card in position leaderPosition
     */
    private Integer leaderPosition;

    public GUI() {
        gamePhase = GamePhases.INITIALIZATION;
        scenes = new HashMap<>();
        controllers = new HashMap<>();
        phases = new HashMap<>();
        fxml = new HashMap<>();
        errorFromServer="";
        leaderEnsure = new HashMap<>();
        players=0;
        leaderCardsDiscarded = new ArrayList<>(Arrays.asList(false , false));
        stockLeaderCardInUse = new ArrayList<>();

        this.isAckArrived = false;
        this.isNackArrived = false;
        this.isUpdateDashboardArrived = false;
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

    /**
     * Method that initializes the hash map relative to the scenes, to the controller, to the phases and to the fxml files
     */
    public void initializationFXMLParameter() {
        List<GameFxml> fxmlFiles = new ArrayList<>(Arrays.asList(GameFxml.values()));
        try {
            for (GameFxml path : fxmlFiles) {
                URL url = getClass().getClassLoader().getResource("fxml/" + path.s);
                FXMLLoader loader = new FXMLLoader(url);
                Scene scene = new Scene(loader.load());
                scenes.put(path.s, scene);
                Controller controller = loader.getController();
                controller.setGui(this);
                controllers.put(path.s, controller);
                phases.put(scene,path.getGamePhases());
                fxml.put(path.getGamePhases(),path.s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = scenes.get(GameFxml.IP_PORT.s);
    }

    public void initializationStage() {
        currentStage.setTitle("Masters of Renaissance");
        currentStage.setScene(currentScene);
        currentStage.getIcons().add(new Image(String.valueOf(getClass().getClassLoader().getResource("images/backgrounds/lorenzo.png"))));
        currentStage.setResizable(false);
        currentStage.show();
    }

    /**
     * Method that changes the scene: set the new scene, initialize the relative controller and show the scene
     */
    public void changeScene() {

        Platform.runLater(()->{
            //System.out.println("Phase is: " + gamePhase);
            currentStage.setScene(currentScene);
            if(gamePhase.equals(GamePhases.SEEOTHERVIEW)) ((ViewEnemyController) controllers.get(fxml.get(gamePhase))).setNickname(otherView);
            controllers.get(fxml.get(gamePhase)).init();
            //System.out.println("show scene " + phases.get(currentScene));
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

    public void setSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
    }

    public void setClientSocket() throws IOException {
            clientSocket = new ClientSocket(this, socket);
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
        if(leaderEnsure!=null )System.out.println(leaderEnsure.size());
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

    public String getOtherView() {
        return otherView;
    }

    public void setOtherView(String otherView) {
        this.otherView = otherView;
    }

    public ArrayList<Boolean> getLeaderCardsDiscarded() {
        return leaderCardsDiscarded;
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

    public boolean isAckArrived() {
        return isAckArrived;
    }

    public void setAckArrived(boolean ackArrived) {
        isAckArrived = ackArrived;
    }

    public boolean isUpdateDashboardArrived() {
        return isUpdateDashboardArrived;
    }

    public void setUpdateDashboardArrived(boolean updateDashboardArrived) {
        isUpdateDashboardArrived = updateDashboardArrived;
    }

    public boolean isNackArrived() {
        return isNackArrived;
    }

    public void setNackArrived(boolean nackArrived) {
        isNackArrived = nackArrived;
    }

    public ArrayList<Integer> getStockLeaderCardInUse() {
        return stockLeaderCardInUse;
    }

    public void addStockLeaderCardInUse(Integer cardNumber){
        System.out.println("Added stockLeaderCardInUse: " + cardNumber);
        this.stockLeaderCardInUse.add(cardNumber);
    }
}