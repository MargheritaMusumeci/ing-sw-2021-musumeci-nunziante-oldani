package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.View;
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

public class GUI extends Application {

    //list of file .fxml
    public static final String IP_PORT = "ip_port_configuration.fxml";
    public static final String NICKNAME = "nickname_configuration.fxml";
    public static final String PLAYERS = "players_configuration.fxml";
    public static final String LEADER_CARD = "leader_cards_configuration.fxml";
    public static final String INITIAL_RESOURCES = "initial_resources.fxml";
    public static final String WAITING_ROOM = "waiting.fxml";

    private Scene currentScene;
    private Stage currentStage;
    private HashMap<String, Scene> scenes;
    private HashMap<String, Controller> controllers;
    private HashMap<Scene, GamePhases> phases;

    private View view;
    private ClientSocket clientSocket;
    private Socket socket;
    private GamePhases gamePhase;
    private String nickname;
    private String errorFromServer;

    public GUI() {
        gamePhase = GamePhases.IINITIALIZATION;
        scenes = new HashMap<>();
        controllers = new HashMap<>();
        phases = new HashMap<>();
    }

    public void initializationFXMLParameter() {
        List<String> fxmlFiles = new ArrayList<>(Arrays.asList(IP_PORT, NICKNAME, PLAYERS, LEADER_CARD, INITIAL_RESOURCES, WAITING_ROOM));
        try {
            for (String path : fxmlFiles) {
                URL url = new File("src/main/resources/fxml/" + path).toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Scene scene = new Scene(loader.load());
                scenes.put(path, scene);
                Controller controller = loader.getController();
                controller.setGui(this);
                phases.put(scene, linkFXMLPageToPhase(path));
                controllers.put(path, controller);
            }
        } catch (IOException e) {
            // logger.log(Level.SEVERE, e.getMessage(), e);
        }
        currentScene = scenes.get(IP_PORT);
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

    public void initializationStage() {
        currentStage.setTitle("Masters of Renaissance");
        currentStage.setScene(currentScene);
        currentStage.show();
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

    public void setGamePhase(GamePhases fase) {
        gamePhase = fase;
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

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void changeScene() {
        Platform.runLater(()->{
                currentStage.setScene(currentScene);
                currentStage.show();
        });
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

    private GamePhases linkFXMLPageToPhase(String NAME) {
        switch (NAME) {
            case (NICKNAME):
                return GamePhases.NICKNAME;
            case (PLAYERS):
                return GamePhases.NUMBEROFPLAYERS;
            case (LEADER_CARD):
                return GamePhases.INITIALLEADERCARDSELECTION;
            case (INITIAL_RESOURCES):
                return GamePhases.INITIALRESOURCESELECTION;
            default:
                return GamePhases.IINITIALIZATION;
        }
    }
}