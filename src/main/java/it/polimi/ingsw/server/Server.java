package it.polimi.ingsw.server;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.utils.Constants;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will handle the correlation between the ServerSoocket and the list of clients created
 */
public class Server {

    private SocketServer socketServer;
    private static int port = 2222; //default value for debugging mode, port should be retrieved from args
    //in the future some hashmap containing the connection from socket ad the game has to be provided

    //The add method should be synchronized in order to avoid errors while adding participants
    private List<ServerClientConnection> lobby4players;
    private List<ServerClientConnection> lobby3players;
    private List<ServerClientConnection> lobby2players;
    private final List<ServerClientConnection> queue; // we should think of a better solution than an array list (MAP)

    private HashMap<ServerClientConnection, GameHandler> games;
    private List<String> listOfTakenNicknames;
    private HashMap<String, ServerClientConnection> waitingForReconnection;
    private HashMap<Game,List<ServerClientConnection>> persistenceWaitingList;
    private ArrayList<String> persistenceNicknameList;

    private Persistence persistence;

    public Server(){
        this.socketServer = new SocketServer(this);
        lobby4players = new ArrayList<>();
        lobby3players = new ArrayList<>();
        lobby2players = new ArrayList<>();
        queue = new ArrayList<>();
        games = new HashMap<>();
        listOfTakenNicknames = new ArrayList<>();
        waitingForReconnection = new HashMap<>();
        this.persistence = new Persistence(this);
        persistenceNicknameList = new ArrayList<>();
        persistenceWaitingList= new HashMap<>();
        persistence.initializeGame();


    }

    public int getPort(){ return port; }

    public SocketServer getSocketServer(){
        return socketServer;
    }

    public List<ServerClientConnection> getQueue(){ return queue; }

    public HashMap<ServerClientConnection, GameHandler> getGames() { return games; }

    public List<String> getListOfTakenNicknames(){ return listOfTakenNicknames;}

    /**
     * method that removes the serverClientConnection from the queue.
     * @param scc
     * @return true if the object is removed correctly, false otherwise
     */
    public boolean removeFromQueue(ServerClientConnection scc){

        for (ServerClientConnection serverClientConnection: queue) {
            if(serverClientConnection.equals(scc)){
                queue.remove(scc);
                return true;
            }
        }

        return false;
    }

    /**
     * method that checks if a nickname is available. It is required because is able to make the check with
     * synchronization
     * @param nickname is the nickname to be checked
     * @return false if is already taken, true if is available
     */
    public boolean checkNickname(String nickname){

        synchronized(listOfTakenNicknames){
            if(listOfTakenNicknames.contains(nickname)){
                return false;
            }else{
                return true;
            }
        }

    }

    /**
     * method that cheks if the player who is trying to play is one of the player that were playing in a game before
     * disconnection
     * @param nickname is the nickname to be evaluated
     * @return the virtual view of the player if exist, null otherwise.
     */
    public ServerClientConnection checkDisconnectedPlayer(String nickname){

        synchronized(waitingForReconnection){
            if(waitingForReconnection.containsKey(nickname)){
                return waitingForReconnection.get(nickname);
            }else{
                return null;
            }
        }

    }

    public synchronized void startSoloGame(ServerClientConnection scc){
        ArrayList<ServerClientConnection> soloPlayer = new ArrayList<>();
        soloPlayer.add(scc);
        GameHandler gameHandler = new GameHandler(1, soloPlayer);
        System.out.println("solo game started");
    }

    public synchronized void addToLobby2Players(ServerClientConnection scc){
        lobby2players.add(scc);
        //check if the lobby is ready to make a new game
        if (lobby2players.size()==2){
            ArrayList<ServerClientConnection> twoPlayers = new ArrayList<>();

            for (int i=0; i<2; i++) {
                twoPlayers.add(lobby2players.remove(0));
            }
            GameHandler gameHandler = new GameHandler(2, twoPlayers);
            for (int i=0; i<2; i++) {
                games.put(twoPlayers.get(i), gameHandler);
            }
        }
        System.out.println("2 players: " + lobby2players.size());
    }

    public synchronized void addToLobby3Players(ServerClientConnection scc){
        lobby3players.add(scc);
        //check if the lobby is ready to make a new game
        if (lobby3players.size()==3){
            ArrayList<ServerClientConnection> threePlayers = new ArrayList<>();

            for (int i=0; i<3; i++) {
                threePlayers.add(lobby3players.remove(0));
            }
            GameHandler gameHandler = new GameHandler(3, threePlayers);
            for (int i=0; i<3; i++) {
                games.put(threePlayers.get(i), gameHandler);
            }

        }
        System.out.println("3 players: " + lobby3players.size());
    }

    public synchronized void addToLobby4Players(ServerClientConnection scc){
        lobby4players.add(scc);
        //check if the lobby is ready to make a new game
        if(lobby4players.size() == 4){
            ArrayList<ServerClientConnection> fourPlayers = new ArrayList<>();
            for (int i=0; i<4; i++) {
                fourPlayers.add(lobby4players.remove(0));
            }
            GameHandler gameHandler = new GameHandler(4, fourPlayers);
            for (int i=0; i<4; i++) {
               games.put(fourPlayers.get(i), gameHandler);
            }
        }
        System.out.println("4 players: " + lobby4players.size());
    }

    public synchronized void removeToLobby2Players(ServerClientConnection scc){
        lobby2players.remove(scc);
    }

    public synchronized void removeToLobby3Players(ServerClientConnection scc){
        lobby2players.remove(scc);
    }

    public synchronized void removeToLobby4Players(ServerClientConnection scc){
        lobby2players.remove(scc);
    }

    public void addTakenNickname(String nickname) {
        synchronized (listOfTakenNicknames){
            listOfTakenNicknames.add(nickname);
        }
    }

    public void removeTakeNickname(String nickname){
        synchronized (listOfTakenNicknames){
            listOfTakenNicknames.remove(nickname);
        }
    }

    public void addWaitingForReconnection(ServerClientConnection scc){
        synchronized (waitingForReconnection){
            waitingForReconnection.put(scc.getNickname(), scc);
        }

    }

    public void removeWaitForReconnection(ServerClientConnection scc) {
        synchronized (waitingForReconnection){
            waitingForReconnection.remove(scc.getNickname());
        }
    }

    public List<ServerClientConnection> getLobby4players() {
        return lobby4players;
    }

    public void setLobby4players(List<ServerClientConnection> lobby4players) {
        this.lobby4players = lobby4players;
    }

    public List<ServerClientConnection> getLobby3players() {
        return lobby3players;
    }

    public void setLobby3players(List<ServerClientConnection> lobby3players) {
        this.lobby3players = lobby3players;
    }

    public List<ServerClientConnection> getLobby2players() {
        return lobby2players;
    }

    public void setLobby2players(List<ServerClientConnection> lobby2players) {
        this.lobby2players = lobby2players;
    }

    public static void main(String[] args){
        System.out.println(Constants.ANSI_RED + "Maestri del Rinascimento | Server" + Constants.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println(Constants.ANSI_CYAN + ">Insert the port which server will listen on." + Constants.ANSI_RESET);
            System.out.print(Constants.ANSI_CYAN + "> " +Constants.ANSI_RESET);
            try {
                port = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Numeric format requested, retry...");
            }
            if(port < 1025 || port > 65535){
                System.err.println("Error: ports available only from 1025 to 65534");
            }
        }while(port < 1025 || port > 65534);

        System.out.println(Constants.ANSI_GREEN + "Starting the server" + Constants.ANSI_RESET);
        Server server = new Server();

        ExecutorService ex = Executors.newCachedThreadPool();
        ex.submit(server.socketServer);

    }

    public Persistence getPersistence() {
        return persistence;
    }

    public void setPersistence(Persistence persistance) {
        this.persistence = persistance;
    }

    public HashMap<Game, List<ServerClientConnection>> getPersistenceWaitingList() {
        return persistenceWaitingList;
    }

    public void setPersistenceWaitingList(HashMap<Game, List<ServerClientConnection>> persistenceWaitingList) {
        this.persistenceWaitingList = persistenceWaitingList;
    }

    public ArrayList<String> getPersistenceNicknameList() {
        return persistenceNicknameList;
    }

    public void setPersistenceNicknameList(ArrayList<String> persistenceNicknameList) {
        this.persistenceNicknameList = persistenceNicknameList;
    }
}
