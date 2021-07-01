package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PingMessage;
import it.polimi.ingsw.messages.sentByServer.AbortGameMessage;
import it.polimi.ingsw.messages.sentByServer.EndGameMessage;
import it.polimi.ingsw.messages.sentByServer.ReconnectionMessage;
import it.polimi.ingsw.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that handles the communication between the server and the client
 */
public class ServerClientConnection implements Runnable{

    private Socket socket;
    private String nickname;
    private Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean isActive = false;
    private PingSender ps;
    private ExecutorService executorService;
    private MessageHandler messageHandler;
    private GamePhases gamePhase;
    private GameHandler gameHandler;
    private Boolean hasDisconnectionBeenCalled;

    /**
     * class constructor that creates the streams, the ping sender and the message handler
     * @param server is the instance of the server
     * @param socket is the socket with the client
     */
    public ServerClientConnection(Server server, Socket socket) throws IOException{
        this.server = server;
        this.socket = socket;
        isActive = true;
        hasDisconnectionBeenCalled = false;
        executorService = Executors.newCachedThreadPool();
        nickname = null;
        messageHandler = new MessageHandler(this.server, this);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        isActive = true;
        ps = new PingSender(this);
        gamePhase = GamePhases.CONFIGURATION;

    }

    /**
     * method able to send a message to the client
     * @param message is the message to be sent
     */
    public synchronized void send(Message message) {
        if(!isActive){
            return;
        }
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * method that wait for messages from the client and than invoke the method to handle it. It also handles the
     * first part of disconnection when ann IOException is threw
     */
    @Override
    public void run() {

        try{
            ps.setActive(true);
            new Thread(ps).start();
            System.out.println("Start with socket: " + socket);
            Message input = null;
            while (isActive){
                //Read input messages and execute them
                input = (Message) inputStream.readObject();

                if(! (input instanceof PingMessage)){
                    System.out.println("Message read: " + input.getMessage() + " From: " + nickname);

                    if(gameHandler != null && gameHandler.getPlayersInGame().get(this) != null){
                        System.err.println(nickname + "is playing: " + gameHandler.getPlayersInGame().get(this).isPlaying());
                    }

                    ((ClientMessage) input).handle(messageHandler);
                }else{
                    ps.pingReceived();
                }

            }
        }catch (IOException e){
            System.out.println(nickname + ": readobject disconnection");
            isActive = false;
            ps.setActive(false);

            synchronized (hasDisconnectionBeenCalled){
                hasDisconnectionBeenCalled = true;
                disconnect();
            }
        }catch (ClassNotFoundException e){
            System.out.println("message sent was not correct");
        }
    }

    /**
     * method that handles the disconnection of a player. Put the player in pause and, in case there aren't players active
     * puts the game in pause.
     */
    public synchronized void disconnect(){


        //check if the player was in a lobby
        checkLobbyDisconnection();

        if(gamePhase.equals(GamePhases.INITIALIZATION)){
            abortGame();
            return;
        }
        //check if the disconnection occurs in a relevant moment of the game
        if(nickname != null && gamePhase!= GamePhases.ENDGAME) {
            server.addWaitingForReconnection(this);
            isActive = false;
            gameHandler.getPlayersInGame().get(this).setPlaying(false);
            gameHandler.getPlayersInGame().get(this).setActionChose(Action.NOTHING);

            //check if there are other players playing in the game
            boolean checkInGame = false;
            for (Player player: gameHandler.getPlayersInGame().values()){
                if (player.isPlaying()){
                    if(!(player instanceof LorenzoPlayer)){
                        checkInGame = true;
                        break;
                    }else{
                        //put lorenzo in wait if there is a lorenzo player
                        player.setPlaying(false);
                    }

                }
            }

            if(!checkInGame){
                System.out.println("Paused the game");
                gameHandler.getGame().setInPause(true);
                return;
            }

            //check if the player was the current player
            if(gameHandler.getGame().getActivePlayer().getNickName().equals(nickname)){ ;
                //the player who was playing the turn has disconnected

                Message messageEndTurn = getGameHandler().getTurnHandler().endTurn();

                if( messageEndTurn instanceof UpdateActivePlayerMessage) {
                    for (ServerClientConnection serverClientConnection: gameHandler.getPlayersInGame().keySet()){
                        serverClientConnection.send(messageEndTurn);
                    }
                }else if (messageEndTurn instanceof EndGameMessage){
                    for (ServerClientConnection serverClientConnection: gameHandler.getPlayersInGame().keySet()){
                        serverClientConnection.send(messageEndTurn);
                    }
                }
            }
        }
    }

    /**
     * if a player disconnect while the game is in the initialization phase, the game ends for
     * all the players
     */
    private void abortGame() {
        //send abort message to every player
        for (ServerClientConnection serverClientConnection: gameHandler.getPlayersInGame().keySet()){
            serverClientConnection.send(new AbortGameMessage("The game is ended due to an early disconnection " +
                    "of a player. Please restart the game"));
        }

        //set game in pause
        gameHandler.getGame().setInPause(true);

        //remove the taken nicknames from the arrayList
        for (Player nickname : gameHandler.getPlayersInGame().values()){
            server.removeTakeNickname(nickname.getNickName());
        }
    }

    /**
     * check if the player that has disconnected was waiting in lobby, in that case removes him
     */
    private void checkLobbyDisconnection() {
        for (ServerClientConnection serverClientConnection : server.getLobby2players()){
            if (serverClientConnection.equals(this)){
                server.removeToLobby2Players(this);
                return;
            }
        }

        for (ServerClientConnection serverClientConnection : server.getLobby3players()){
            if (serverClientConnection.equals(this)){
                server.removeToLobby3Players(this);
                return;
            }
        }

        for (ServerClientConnection serverClientConnection : server.getLobby4players()){
            if (serverClientConnection.equals(this)){
                server.removeToLobby4Players(this);
                return;
            }
        }
    }

    /**
     * method that handles the reconnection od a player
     * @param socket is the new socket with the client
     * @param inputStream is the new input stream reader
     * @param outputStream is the new output stream reader
     * @return true if the player is correctly reconnected
     */
    public boolean reconnect(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream){
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;

        if(gameHandler.getGame().isInPause()){
            //reconnection of the first player is handled in a separate method
            return reconnectFirstPlayer();
        }

        //i need to send the virtual view to the player to "update" him
        isActive = true;
        send(new ReconnectionMessage("", gameHandler.createView(gameHandler.getPlayerSockets().get(gameHandler.getPlayersInGame().get(this))), gameHandler.getNumberOfPlayers()));
        gameHandler.getPlayersInGame().get(this).setPlaying(true);
        new Thread(this).start();
        return true;
    }

    /**
     * method that handles the reconnection of the first player that rejoins the game
     * @return true if the reconnection is done correctly
     */
    private boolean reconnectFirstPlayer() {
        isActive = true;
        //restart the game that was in pause
        gameHandler.getGame().setInPause(false);
        //send the message to the player
        send(new ReconnectionMessage("", gameHandler.createView(gameHandler.getPlayerSockets().get(gameHandler.getPlayersInGame().get(this))), gameHandler.getNumberOfPlayers()));
        //reactivate the player
        gameHandler.getPlayersInGame().get(this).setPlaying(true);
        new Thread(this).start();
        //update the active player
        gameHandler.getGame().updateActivePlayer();
        if(gameHandler.getGame().getActivePlayer() instanceof LorenzoPlayer){
            gameHandler.getGame().updateActivePlayer();
        }
        //send message with the new active player to the client
        send(new UpdateActivePlayerMessage(nickname));
        return true;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public GamePhases getGamePhase() {
        return gamePhase;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void setGamePhase(GamePhases gamePhase) {
        this.gamePhase = gamePhase;
    }

    public boolean isActive(){return isActive;}

    public void setActive(boolean value){
        this.isActive = value;
    }

    public Boolean getHasDisconnectionBeenCalled() {
        return hasDisconnectionBeenCalled;
    }

    public void setHasDisconnectionBeenCalled(Boolean hasDisconnectionBeenCalled) {
        this.hasDisconnectionBeenCalled = hasDisconnectionBeenCalled;
    }


    public Socket getSocket() {
        return socket;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

}
