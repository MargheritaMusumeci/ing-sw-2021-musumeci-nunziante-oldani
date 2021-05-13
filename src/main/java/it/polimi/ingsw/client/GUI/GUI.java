package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.client.GUI.controllers.IpPortConfigurationController;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;


public class GUI extends Application {

    private View view;
    private ClientSocket clientSocket;
    private GamePhases gamePhase;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String nickname;
    private int numberOfPlayers;
    private Socket socket;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private ArrayList<Resource> resources;
    private boolean serverIsUp;

    public GUI(){
        isNackArrived = false;
        isAckArrived = false;
        leaderCards = null;
        resources = null;
        gamePhase = GamePhases.IINITIALIZATION;
    }
    public static void main(String[] args){

        launch(args);
        new GUI();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket() {
        try {
            clientSocket = new ClientSocket(this, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public GamePhases getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhases gamePhase) {
        this.gamePhase = gamePhase;
    }

    @Override
    public void start(Stage stage) throws Exception {

        //Controller ipPortConfigurationController = new IpPortConfigurationController();

        URL url = new File("src/main/resources/fxml/ip_port_configuration.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        stage.setTitle("Masters of Renaissance");
        stage.setScene(new Scene(loader.load()));
        Controller ipPortConfigurationController = loader.getController();
        ipPortConfigurationController.setGui(this);
        stage.show();
    }
}
