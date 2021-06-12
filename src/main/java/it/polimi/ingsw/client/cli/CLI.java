package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.componentPrinter.*;
import it.polimi.ingsw.client.ClientSocket;

import it.polimi.ingsw.client.cli.gamePhases.InitializationPhase;
import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.*;
import it.polimi.ingsw.utils.Constants;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI implements Runnable {

    private Scanner scanner;
    private ClientSocket clientSocket;
    private Phase gamePhase;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String nickname;
    private Socket socket;
    private int numberOfPlayers;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private ArrayList<Resource> resources;
    private boolean serverIsUp;
    private boolean isActionBeenDone;

    private SerializableMarket temporaryMarket;
    private SerializableEvolutionSection temporaryEvolutionSection;

    public CLI(){
        scanner = new Scanner(System.in);
        isNackArrived = false;
        isAckArrived = false;
        leaderCards = null;
        resources = null;
        gamePhase = new InitializationPhase();
        isActionBeenDone = false;
        new Thread(this).start();
    }

    //metodi per stampare le componenti del gioco
    public void printTitle(){

        System.out.println(Constants.ANSI_RED + Constants.title + Constants.ANSI_RESET);
    }
    public void printMenu(){
        System.out.println(Constants.menu);

        System.out.print(Constants.ANSI_CYAN + "Choose your action: " + Constants.ANSI_RESET);
    }

    public void printLockBox(){
        SerializableLockBox lockBox =  clientSocket.getView().getDashboard().getSerializableLockBox();
        LockBoxPrinter.print(lockBox);

    }
    public void printStock(){
        SerializableStock stock = clientSocket.getView().getDashboard().getSerializableStock();
        StockPrinter.print(stock);
    }
    public void printMarket(){
        SerializableMarket market = clientSocket.getView().getMarket();
        MarketPrinter.print(market);
    }
    public void printTemporaryMarket(){
        MarketPrinter.print(temporaryMarket);
    }
    public void printPopeTrack(){
        SerializablePopeTack popeTack = clientSocket.getView().getDashboard().getSerializablePopeTack();
        PopeTrackPrinter.print(popeTack);

    }
    public void printLeaderCards(){
        ArrayList<SerializableLeaderCard> leaderCards = clientSocket.getView().getLeaderCards();
        LeaderCardsPrinter.print(leaderCards);

    }
    public void printProductionZones(){
        ProductionSectionPrinter.print(clientSocket.getView().getDashboard());

    }
    public void printEvolutionSection(){
        SerializableEvolutionSection evolutionSection = clientSocket.getView().getEvolutionSection();
        EvolutionSectionPrinter.print(evolutionSection);
    }
    public void printTemporaryEvolutionSection(){
        EvolutionSectionPrinter.print(temporaryEvolutionSection);
    }
    private void printEvolutionCard(EvolutionCard evolutionCard){
        System.out.println("Color: " + evolutionCard.getColor());
        System.out.println("Level: " + evolutionCard.getLevel());
        System.out.println("Point: " + evolutionCard.getPoint());
        System.out.println("Is active: " + evolutionCard.isActive());
        System.out.println("Cost: ");
        for(Resource resource : evolutionCard.getCost().keySet()){
            System.out.println("    Resource: " + resource + " , quantity: " + evolutionCard.getCost().get(resource));
        }
        System.out.println("Requires: ");
        for(Resource resource : evolutionCard.getRequires().keySet()){
            System.out.println("    Resource: " + resource + " , quantity: " + evolutionCard.getRequires().get(resource));
        }
        System.out.println("Products: ");
        for(Resource resource : evolutionCard.getProduction().keySet()){
            System.out.println("    Resource: " + resource + " , quantity: " + evolutionCard.getProduction().get(resource));
        }
    }
    public void printSetOfLeaderCard(ArrayList<SerializableLeaderCard> leaderCards){

        LeaderCardsPrinter.print(leaderCards);

    }


    public void setIsAckArrived(boolean value){
        isAckArrived = value;
    }

    public void setIsNackArrived(boolean value){
        isNackArrived = value;
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

    public void setServerIsUp(boolean serverIsUp) {
        this.serverIsUp = serverIsUp;
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

    public SerializableMarket getTemporaryMarket() {
        return temporaryMarket;
    }

    public void setTemporaryMarket(SerializableMarket temporaryMarket) {
        this.temporaryMarket = temporaryMarket;
    }

    public SerializableEvolutionSection getTemporaryEvolutionSection() {
        return temporaryEvolutionSection;
    }

    public void setTemporaryEvolutionSection(SerializableEvolutionSection temporaryEvolutionSection) {
        this.temporaryEvolutionSection = temporaryEvolutionSection;
    }

    /*
    public static void main(String[] args){
        new CLI();
    }

     */

    @Override
    public void run() {
        gamePhase.makeAction(this);
    }



}
