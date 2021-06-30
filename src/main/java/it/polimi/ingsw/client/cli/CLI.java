package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.componentPrinter.*;
import it.polimi.ingsw.client.ClientSocket;

import it.polimi.ingsw.client.cli.gamePhases.InitializationPhase;
import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.*;
import it.polimi.ingsw.utils.Constants;

import java.net.Socket;
import java.util.ArrayList;

/**
 * class responsible to coordinate the client execution if the cli interface is
 * selected
 */
public class CLI implements Runnable {

    private ClientSocket clientSocket;
    private Phase gamePhase;
    private boolean isAckArrived;
    private String nickname;
    private Socket socket;
    private int numberOfPlayers;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private ArrayList<Resource> resources;
    private boolean isActionBeenDone;

    private SerializableMarket temporaryMarket;
    private SerializableEvolutionSection temporaryEvolutionSection;

    /**
     * class constructor that starts the client thread with the first phase
     */
    public CLI(){

        isAckArrived = false;
        leaderCards = null;
        resources = null;
        gamePhase = new InitializationPhase();
        isActionBeenDone = false;
        new Thread(this).start();
    }

    /**
     * method that prints the title of the game
     */
    public void printTitle(){

        System.out.println(Constants.ANSI_RED + Constants.title + Constants.ANSI_RESET);
    }

    /**
     * method that prints the menu that contains the action that a player can perform in his turn
     */
    public void printMenu(){
        System.out.println(Constants.menu);

        System.out.print(Constants.ANSI_CYAN + "Choose your action: " + Constants.ANSI_RESET);
    }

    /**
     * method that calls the correct printer to print the player's lock box
     */
    public void printLockBox(){
        SerializableLockBox lockBox =  clientSocket.getView().getDashboard().getSerializableLockBox();
        LockBoxPrinter.print(lockBox);

    }

    /**
     * method that calls the correct printer to print the player's stock
     */
    public void printStock(){
        SerializableStock stock = clientSocket.getView().getDashboard().getSerializableStock();
        StockPrinter.print(stock);
    }

    /**
     * method that calls the correct printer to print the market
     */
    public void printMarket(){
        SerializableMarket market = clientSocket.getView().getMarket();
        MarketPrinter.print(market);
    }

    /**
     * method that calls the correct printer to print the market before the game is started
     */
    public void printTemporaryMarket(){
        MarketPrinter.print(temporaryMarket);
    }

    /**
     * method that calls the correct printer to print the player's pope track
     */
    public void printPopeTrack(){
        SerializablePopeTack popeTack = clientSocket.getView().getDashboard().getSerializablePopeTack();
        PopeTrackPrinter.print(popeTack);

    }

    /**
     * method that calls the correct printer to print the player's leader cards
     */
    public void printLeaderCards(){
        ArrayList<SerializableLeaderCard> leaderCards = clientSocket.getView().getLeaderCards();
        LeaderCardsPrinter.print(leaderCards);

    }

    /**
     * method that calls the correct printer to print the player's production zones
     */
    public void printProductionZones(){
        ProductionSectionPrinter.print(clientSocket.getView().getDashboard());

    }

    /**
     * method that calls the correct printer to print the evolution section
     */
    public void printEvolutionSection(){
        SerializableEvolutionSection evolutionSection = clientSocket.getView().getEvolutionSection();
        EvolutionSectionPrinter.print(evolutionSection);
    }

    /**
     * method that calls the correct printer to print the evolution section before the game is started
     */
    public void printTemporaryEvolutionSection(){
        EvolutionSectionPrinter.print(temporaryEvolutionSection);
    }

    /**
     * method that calls the correct printer to print the player's set of leader cards
     */
    public void printSetOfLeaderCard(ArrayList<SerializableLeaderCard> leaderCards){
        LeaderCardsPrinter.print(leaderCards);
    }


    public void setIsAckArrived(boolean value){
        isAckArrived = value;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards){ this.leaderCards = leaderCards; }

    public void setResources(ArrayList<Resource> resources){ this.resources = resources; }

    public void setGamePhase(Phase gamePhase){
        this.gamePhase = gamePhase;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public boolean isAckArrived() {
        return isAckArrived;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public boolean isActionBeenDone() {
        return isActionBeenDone;
    }

    public void setActionBeenDone(boolean actionBeenDone) {
        isActionBeenDone = actionBeenDone;
    }

    public Phase getGamePhase() {
        return gamePhase;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public void setTemporaryMarket(SerializableMarket temporaryMarket) {
        this.temporaryMarket = temporaryMarket;
    }


    public void setTemporaryEvolutionSection(SerializableEvolutionSection temporaryEvolutionSection) {
        this.temporaryEvolutionSection = temporaryEvolutionSection;
    }

    /**
     * calls the methods doAction of the current game phase and pass itself to that
     */
    @Override
    public void run() {
        gamePhase.makeAction(this);
    }



}
