package it.polimi.ingsw.client.messageHandler;

import it.polimi.ingsw.client.CLI.CLI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.gamePhases.EndGamePhase;
import it.polimi.ingsw.client.gamePhases.InitialLeaderCardSelectionPhase;
import it.polimi.ingsw.client.gamePhases.InitialResourcesSelection;
import it.polimi.ingsw.client.gamePhases.myTurnPhases.MyTurnPhase;
import it.polimi.ingsw.client.gamePhases.OtherPlayersTurnPhase;
import it.polimi.ingsw.messages.sentByClient.ExitGameMessage;
import it.polimi.ingsw.messages.sentByServer.*;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.*;
import it.polimi.ingsw.messages.sentByServer.updateMessages.*;
import it.polimi.ingsw.utils.Constants;

public class MessageHandlerCLI extends MessageHandler{

    private CLI cli;

    public MessageHandlerCLI(CLI cli, ClientSocket clientSocket){
        this.cli = cli;
        this.clientSocket = clientSocket;
    }

    @Override
    public void handleMessage(ACKMessage message) {
        cli.setIsAckArrived(true);
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(NACKMessage message) {
        System.out.println(Constants.ANSI_RED + message.getMessage() + Constants.ANSI_RESET);
        cli.setIsNackArrived(true);

        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(ReconnectionMessage message) {
        System.out.println("reconnection message arrivato");
        cli.getClientSocket().setView(message.getView());
        if(message.getView().getActivePlayer().equals(cli.getNickname())){
            cli.setGamePhase(new MyTurnPhase());
        }else{
            cli.setGamePhase(new OtherPlayersTurnPhase());
        }

        //devo vedere se ho tutto settato ma penso di si
        //forse manca solo il number of players

        new Thread(cli).start();
    }

    @Override
    public void handleMessage(StartGameMessage message) {

        cli.setGamePhase(new InitialResourcesSelection());
        new Thread(cli).start();
    }

    @Override
    public void handleMessage(FourLeaderCardsMessage message) {

        cli.setLeaderCards(message.getLeaderCards());
        cli.setGamePhase(new InitialLeaderCardSelectionPhase());
        new Thread(cli).start();

        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(InitialResourcesMessage message) {

        cli.setResources(message.getResources());

    }

    @Override
    public void handleMessage(SendViewMessage message) {

        clientSocket.setView(message.getView());

        if(clientSocket.getView().getActivePlayer().equals(clientSocket.getView().getNickname())){
            cli.setGamePhase(new MyTurnPhase());
        }else {
            cli.setGamePhase(new OtherPlayersTurnPhase());
        }
        new Thread(cli).start();

        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(SendResourcesBoughtFromMarket message) {
        clientSocket.getView().setResourcesBoughtFromMarker(message.getResources());
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleMessage(EndGameMessage message) {
        System.out.println("End game message received");
        cli.getClientSocket().getView().setScores(message.getScores());
        cli.getClientSocket().getView().setWinners(message.getWinners());
        cli.setGamePhase(new EndGamePhase());
        new Thread(cli).start();

        cli.getClientSocket().send(new ExitGameMessage("exit game"));
    }

    @Override
    public void handleUpdateMessage(UpdateLeaderCardsMessage message) {
        clientSocket.getView().setLeaderCards(message.getLeaderCards());
    }

    @Override
    public void handleUpdateMessage(UpdateDashBoardMessage message) {
        clientSocket.getView().setDashboard(message.getDashboard());
    }

    @Override
    public void handleUpdateMessage(UpdateActivePlayerMessage message) {
        clientSocket.getView().setActivePlayer(message.getMessage());
        cli.setActionBeenDone(false);
        if(clientSocket.getView().getNickname().equals(clientSocket.getView().getActivePlayer())){
            //It's my turn
            cli.setGamePhase(new MyTurnPhase());
        }else {
            cli.setGamePhase(new OtherPlayersTurnPhase());
        }
        new Thread(cli).start();

        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    @Override
    public void handleUpdateMessage(UpdateEvolutionSectionMessage message) {
        clientSocket.getView().setEvolutionSection(message.getEvolutionSection());
    }

    @Override
    public void handleUpdateMessage(UpdateMarketMessage message) {
        clientSocket.getView().setMarket(message.getMarket());
    }

    @Override
    public void handleUpdateMessage(UpdateOtherPlayerViewMessage message){
        clientSocket.getView().setEnemyDashboard(message.getView().getDashboard(), message.getNickname());
        clientSocket.getView().setEnemyActivatedLeaderCards(message.getView().getDashboard(), message.getView().getLeaderCards());

        System.out.println("Ho aggioranto la view di un nemico");
    }

    @Override
    public void handleMessage(AbortGameMessage abortGameMessage) {
        System.out.println("GAME ABORTED");
    }

    @Override
    public void handleMessage(MarketAndEvolutionSectionMessage marketMessage) {

    }


}
