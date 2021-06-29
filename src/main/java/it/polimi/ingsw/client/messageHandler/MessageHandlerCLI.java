package it.polimi.ingsw.client.messageHandler;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.cli.gamePhases.*;
import it.polimi.ingsw.client.cli.gamePhases.myTurnPhases.MyTurnPhase;
import it.polimi.ingsw.messages.sentByClient.ExitGameMessage;
import it.polimi.ingsw.messages.sentByServer.*;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.*;
import it.polimi.ingsw.messages.sentByServer.updateMessages.*;
import it.polimi.ingsw.utils.Constants;

/**
 * class that handles all the messages sent by the server implemented for the cli
 */
public class MessageHandlerCLI extends MessageHandler{

    private CLI cli;

    /**
     * class constructor
     * @param cli is the client's cli
     * @param clientSocket is the instance of the clint socket that handles the connection with the server
     */
    public MessageHandlerCLI(CLI cli, ClientSocket clientSocket){
        this.cli = cli;
        this.clientSocket = clientSocket;
    }

    /**
     * method able to handle the generic ACK message, it's responsible to notify all the thread waiting
     * @param message does not contains useful information for the user
     */
    @Override
    public void handleMessage(ACKMessage message) {
        cli.setIsAckArrived(true);
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    /**
     * method able to handle the generic NACK message, it's responsible to write the error and to notify
     * all the thread waiting
     * @param message contains the error occurred server side
     */
    @Override
    public void handleMessage(NACKMessage message) {
        System.out.println(Constants.ANSI_RED + message.getMessage() + Constants.ANSI_RESET);
        cli.setIsNackArrived(true);

        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    /**
     * method able to handle the reconnection message, it is responsible to set the Phase based on the active player
     * received in the new view. It also sets the view
     * @param message contains the player's view
     */
    @Override
    public void handleMessage(ReconnectionMessage message) {
        System.out.println("reconnection message arrivato");
        cli.getClientSocket().setView(message.getView());
        if(message.getView().getActivePlayer().equals(cli.getNickname())){
            cli.setGamePhase(new MyTurnPhase());
        }else{
            cli.setGamePhase(new OtherPlayersTurnPhase());
        }

        cli.setNumberOfPlayers(message.getNumberOfPlayers());

        new Thread(cli).start();
    }

    /**
     * method able to handle the message sent by the server when everything is ready to start the game.
     * It will set the game phase to the selection of the initial resources
     * @param message does not contains useful information for the user
     */
    @Override
    public void handleMessage(StartGameMessage message) {

        cli.setGamePhase(new InitialResourcesSelection());
        new Thread(cli).start();
    }

    /**
     * method able to handle the message containing the leader cards from which the user has to choose
     * @param message does not contains the leader cards, they are sent with an update message
     */
    @Override
    public void handleMessage(FourLeaderCardsMessage message) {

        cli.setLeaderCards(message.getLeaderCards());
        cli.setGamePhase(new InitialLeaderCardSelectionPhase());
        new Thread(cli).start();

        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    /**
     * method able to handle the initial resource message. It is responsible to actually set the resources in the
     * player's view
     * @param message contains the resources from which the user has to choose
     */
    @Override
    public void handleMessage(InitialResourcesMessage message) {

        cli.setResources(message.getResources());

    }

    /**
     * method able to handle the message that contains the player's view. It will also set the phase to MyTurnPhase or
     * OtherPlayersTurn based on the nickname of the active player received in the view
     * @param message contains the player's view
     */
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

    /**
     * method able to handle the message that contains the resources that a player has bought from the market.
     * It will notify all the thead waiting to finish the action
     * @param message contains the set of resources
     */
    @Override
    public void handleMessage(SendResourcesBoughtFromMarket message) {
        clientSocket.getView().setResourcesBoughtFromMarker(message.getResources());
        synchronized (cli.getGamePhase()){
            cli.getGamePhase().notifyAll();
        }
    }

    /**
     * method able to handle the end of the game message. It will set the phase to EndGamePhase and then
     * sent back to the server the message to exit the game
     * @param message contains both the players' scores and the nickname of the winner
     */
    @Override
    public void handleMessage(EndGameMessage message) {
        System.out.println("End game message received");
        cli.getClientSocket().getView().setScores(message.getScores());
        cli.getClientSocket().getView().setWinners(message.getWinners());
        cli.setGamePhase(new EndGamePhase());
        new Thread(cli).start();

        cli.getClientSocket().send(new ExitGameMessage("exit game"));
    }

    /**
     * method able to handle the persistence message. It will pause the client putting it into the WaitingforOtherPlayer
     * phase in order to wait all the player after the game has been restored server side
     * @param message does not contains useful information for the user
     */
    @Override
    public void handleMessage(PersistenceMessage message) {
        cli.setGamePhase(new WaitingOtherPlayersPhase());
        System.out.println(Constants.ANSI_GREEN + "Persistence is recreating your game" + Constants.ANSI_RESET);
        new Thread(cli).start();
    }

    /**
     * method able to handle the message that updates the player's leader cards
     * @param message contains the new set of leader cards
     */
    @Override
    public void handleUpdateMessage(UpdateLeaderCardsMessage message) {
        clientSocket.getView().setLeaderCards(message.getLeaderCards());
    }

    /**
     * method able to handle the message that updates the player's dashboard
     * @param message contains the new dashboard
     */
    @Override
    public void handleUpdateMessage(UpdateDashBoardMessage message) {
        clientSocket.getView().setDashboard(message.getDashboard());
    }

    /**
     * method able to handle the message that updates the active player
     * @param message contains the new active player
     */
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

    /**
     * method able to handle the message that updates the evolution section
     * @param message contains the new evolution section
     */
    @Override
    public void handleUpdateMessage(UpdateEvolutionSectionMessage message) {
        clientSocket.getView().setEvolutionSection(message.getEvolutionSection());
    }

    /**
     * method able to handle the message that updates the market
     * @param message contains the new market
     */
    @Override
    public void handleUpdateMessage(UpdateMarketMessage message) {
        clientSocket.getView().setMarket(message.getMarket());
    }

    /**
     * method able to handle the message that updates the other player's view
     * @param message contains the new set of other players views
     */
    @Override
    public void handleUpdateMessage(UpdateOtherPlayerViewMessage message){
        clientSocket.getView().setEnemyDashboard(message.getView().getDashboard(), message.getNickname());
        clientSocket.getView().setEnemyActivatedLeaderCards(message.getView().getDashboard(), message.getView().getLeaderCards());

    }

    /**
     * method that close the application when this message arrives displaying the corresponding error.
     * This message arrives when a player exit the game during the initialization phase
     * @param abortGameMessage
     */
    @Override
    public void handleMessage(AbortGameMessage abortGameMessage) {
        System.out.println(Constants.ANSI_RED + abortGameMessage.getMessage() + Constants.ANSI_RESET);
        System.exit(0);
    }

    /**
     * method able to handle the market and evolution section initialization before the game is started in order
     * to display them during the selection of the leader cards and the initial resources.
     * @param marketMessage contains the market and the evolution section (Serializable version)
     */
    @Override
    public void handleMessage(MarketAndEvolutionSectionMessage marketMessage) {

        cli.setTemporaryMarket(marketMessage.getSerializableMarket());
        cli.setTemporaryEvolutionSection(marketMessage.getSerializableEvolutionSection());
    }


}
