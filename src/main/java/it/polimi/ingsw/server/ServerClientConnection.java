package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PingMessage;
import it.polimi.ingsw.messages.sentByClient.ClientMessage;
import it.polimi.ingsw.model.game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;
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
        messageHandler = new MessageHandler(this.server);
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
            new Thread(ps).start();
            System.out.println("Start with socket: " + socket);
            Message input = null;
            while (isActive){
                //Read input messages and execute them
                if(input == null)
                    System.out.println("Input stream rotto!!!!");
                input = (Message) inputStream.readObject();
                if(! (input instanceof PingMessage)){
                    System.out.println("Message read: " + input.getMessage());
                    messageHandler.handleMessage(input, this);
                }else{
                    ps.pingRecived();
                }
            }
        }catch (IOException e){
            System.out.println(nickname + ": disconnesso nel readObject");
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

    public void disconnect(){
        if(nickname != null){
            server.addWaitingForReconnection(this);
            isActive = false;
        }
    }

    public boolean reconnect(Socket socket){
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("error while restarting scc");
            return false;
        }
        //vede se gestire qui la riattivazione del game handler corrispondente
        isActive = true;
        new Thread(this).start();
        return true;
    }

    public Socket getSocket() {
        return socket;
    }
}
