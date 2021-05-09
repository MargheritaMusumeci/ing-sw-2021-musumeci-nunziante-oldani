package it.polimi.ingsw.server;

import it.polimi.ingsw.server.virtualView.VirtualView;
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
    private List<ServerClientConnection> queue; // we should think of a better solution than an array list (MAP)
    private List<GameHandler> games;
    private List<String> listOfTakenNicknames;
    private HashMap<String, VirtualView> waitingForReconnection;

    public Server(){
        this.socketServer = new SocketServer(this);
        lobby4players = new ArrayList<>();
        lobby3players = new ArrayList<>();
        lobby2players = new ArrayList<>();
        queue = new ArrayList<>();
        games = new ArrayList<>();
        listOfTakenNicknames = new ArrayList<>();
        waitingForReconnection = new HashMap<>();
    }

    public int getPort(){ return port; }

    public SocketServer getSocketServer(){
        return socketServer;
    }

    public List<ServerClientConnection> getLobby4players() {
        return lobby4players;
    }

    public List<ServerClientConnection> getLobby3players() {
        return lobby3players;
    }

    public List<ServerClientConnection> getLobby2players() {
        return lobby2players;
    }

    public List<ServerClientConnection> getQueue(){ return queue; }

    public List<GameHandler> getGames() { return games; }

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
    public synchronized boolean checkNickname(String nickname){

        if(listOfTakenNicknames.contains(nickname)){
            return false;
        }else{
            return true;
        }
    }

    /**
     * method that cheks if the player who is trying to play is one of the player that were playing in a game before
     * disconnection
     * @param nickname is the nickname to be evaluated
     * @return the virtual view of the player if exist, null otherwise.
     */
    public synchronized VirtualView checkDisconnectedPlayer(String nickname){

        if(waitingForReconnection.containsKey(nickname)){
            return waitingForReconnection.get(nickname);
        }else{
            return null;
        }
    }

    public synchronized void addToLobby2Players(ServerClientConnection scc){
        lobby2players.add(scc);
        //check if the lobby is ready to make a new game
        System.out.println("2 players: " + lobby2players.size());
    }

    public synchronized void addToLobby3Players(ServerClientConnection scc){
        lobby3players.add(scc);
        //check if the lobby is ready to make a new game
        System.out.println("3 players: " + lobby3players.size());
    }

    public synchronized void addToLobby4Players(ServerClientConnection scc){
        lobby4players.add(scc);
        //check if the lobby is ready to make a new game
        System.out.println("4 players: " + lobby4players.size());
    }

    public static void main(String[] args){
        System.out.println(Constants.ANSI_CYAN + "Maestri del Rinascimento | Server" + Constants.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println(">Insert the port which server will listen on.");
            System.out.print(">");
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




}
