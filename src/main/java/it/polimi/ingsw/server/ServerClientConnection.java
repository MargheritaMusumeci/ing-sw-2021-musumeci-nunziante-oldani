package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PingMessage;
import it.polimi.ingsw.messages.sentByServer.ReconnectionMessage;
import it.polimi.ingsw.messages.sentByClient.ClientMessage;

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


    public ServerClientConnection(Server server, Socket socket) throws IOException{
        this.server = server;
        this.socket = socket;
        isActive = true;
        executorService = Executors.newCachedThreadPool();
        nickname = null;
        messageHandler = new MessageHandler(this.server, this);
        //System.out.println("trying to create streams for socket: " + socket);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        //System.out.println("output stream created");
        inputStream = new ObjectInputStream(socket.getInputStream());
        //System.out.println("input stream created");
        isActive = true;
        ps = new PingSender(this);
        gamePhase = GamePhases.CONFIGURATION;

    }

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
            close();
        }
    }

    public void close(){
        //method that delete the socket and if the socket was in a game calls the method to save the satus in order to accept a reconnection
        System.out.println(nickname + ": disconnesso, close() chiamata");
    }

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
                    ps.pingRecived();
                }

            }
        }catch (IOException e){
            System.out.println(nickname + ": disconnesso nel readObject");
            isActive = false;
            ps.setActive(false);
        }catch (ClassNotFoundException e){
            System.out.println("message sent was not correct");
        }
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

    public PingSender getPingSender() { return ps;}

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

    public void disconnect(){

        //se il nickname non era scelto o sono nel fine gioco non devo aggiungerlo alla lista di chi si vuole riconnettere
        if(nickname != null && gamePhase!= GamePhases.ENDGAME) {
            server.addWaitingForReconnection(this);
            isActive = false;
            gameHandler.getPlayersInGame().get(this).setPlaying(false);
        }
    }

    public boolean reconnect(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream){
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;

        //devo mandare la virtual view al giocatore per "aggiornarlo"
        isActive = true;
        send(new ReconnectionMessage("", gameHandler.createView(gameHandler.getPlayerSockets().get(gameHandler.getPlayersInGame().get(this)))));
        //vede se gestire qui la riattivazione del game handler corrispondente
        gameHandler.getPlayersInGame().get(this).setPlaying(true);
        new Thread(this).start();
        return true;
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

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
