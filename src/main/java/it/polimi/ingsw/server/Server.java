package it.polimi.ingsw.server;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.utils.Constants;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will handle the correlation between the ServerSocket and the list of clients created
 */
public class Server {

    private final SocketServer socketServer;
    private static int port = 2222; //default value

    //The add method should be synchronized in order to avoid errors while adding participants
    private List<ServerClientConnection> lobby4players;
    private List<ServerClientConnection> lobby3players;
    private List<ServerClientConnection> lobby2players;
    private final List<ServerClientConnection> queue;

    private HashMap<ServerClientConnection, GameHandler> games;
    private List<String> listOfTakenNicknames;
    private HashMap<String, ServerClientConnection> waitingForReconnection;
    private HashMap<Game,List<ServerClientConnection>> persistenceWaitingList;
    private HashMap<ServerClientConnection, HumanPlayer> playersInGamePersistence;
    HashMap<HumanPlayer,ServerClientConnection> sccRelateToPlayerPersistence;
    private ArrayList<String> persistenceNicknameList;

    private Persistence persistence;

    /**
     * class constructor. Creates all the object needed and starts the persistence method to check if there are games
     * left unfinished
     */
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
        playersInGamePersistence = new HashMap<>();
        sccRelateToPlayerPersistence = new HashMap<>();
        persistence.initializeGame();

    }

    /**
     * method that removes the serverClientConnection from the queue.
     * @param scc is the scc of the player to be removed
     * @return true if the object is removed correctly, false otherwise
     */
    //TODO check if this method can be removed and when this attribute is used
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
            return !listOfTakenNicknames.contains(nickname);
        }

    }

    /**
     * method that checks if the player who is trying to play is one of the player that were playing in a game before
     * disconnection
     * @param nickname is the nickname to be evaluated
     * @return the virtual view of the player if exist, null otherwise.
     */
    public ServerClientConnection checkDisconnectedPlayer(String nickname){

        synchronized(waitingForReconnection){
            return waitingForReconnection.getOrDefault(nickname, null);
        }

    }

    /**
     * method able to start a solo game instantiating the game handler
     * @param scc is the scc of the single player
     */
    public synchronized void startSoloGame(ServerClientConnection scc){
        ArrayList<ServerClientConnection> soloPlayer = new ArrayList<>();
        soloPlayer.add(scc);
        GameHandler gameHandler = new GameHandler(1, soloPlayer);
        System.out.println("solo game started");
    }

    /**
     * method that adds the new player to the lobby where he will wait for another player.
     * If the arraylist reaches size()==2 the method will instantiate a gameHandler to start the game
     * @param scc is the new player' scc
     */
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

    /**
     * method that adds the new player to the lobby where he will wait for other players.
     * If the arraylist reaches size()==3 the method will instantiate a gameHandler to start the game
     * @param scc is the new player' scc
     */
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

    /**
     * method that adds the new player to the lobby where he will wait for other players.
     * If the arraylist reaches size()==4 the method will instantiate a gameHandler to start the game
     * @param scc is the new player' scc
     */
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

    /**
     * remove players from the corresponding lobby
     * @param scc is the connection with the player to be removed
     */
    public synchronized void removeToLobby2Players(ServerClientConnection scc){
        lobby2players.remove(scc);
    }

    /**
     * remove players from the corresponding lobby
     * @param scc is the connection with the player to be removed
     */
    public synchronized void removeToLobby3Players(ServerClientConnection scc){
        lobby2players.remove(scc);
    }

    /**
     * remove players from the corresponding lobby
     * @param scc is the connection with the player to be removed
     */
    public synchronized void removeToLobby4Players(ServerClientConnection scc){
        lobby2players.remove(scc);
    }

    /**
     * add the nickname to the list of taken nicknames
     * @param nickname is the nickname to be added
     */
    public void addTakenNickname(String nickname) {
        synchronized (listOfTakenNicknames){
            listOfTakenNicknames.add(nickname);
        }
    }

    /**
     * remove the nickname to the list of taken nicknames
     * @param nickname is the nickname to be removed
     */
    public void removeTakeNickname(String nickname){
        synchronized (listOfTakenNicknames){
            listOfTakenNicknames.remove(nickname);
        }
    }

    /**
     * method that add the scc to the hashmap of nicknames-scc that are waiting for a reconnection
     * @param scc is the scc representing the player that has disconnected
     */
    public void addWaitingForReconnection(ServerClientConnection scc){
        synchronized (waitingForReconnection){
            waitingForReconnection.put(scc.getNickname(), scc);
        }

    }

    /**
     * method that removes a scc from the hashmap of scc waiting for reconnection
     * @param scc is the scc to be removed
     */
    public void removeWaitForReconnection(ServerClientConnection scc) {
        synchronized (waitingForReconnection){
            waitingForReconnection.remove(scc.getNickname());
        }
    }

    /**
     * method that updates the reconnected players of a game with persistence and if the number of player needed is reached
     * it will reset a gameHandler and call the method to initialize the views
     * @param nickname is the nickname of the player that has reconnected
     * @param scc is the scc of the player that has reconnected
     */
    public void updatePersistenceReconnections(String nickname, ServerClientConnection scc) {

        try{
            persistenceNicknameList.remove(nickname);
            Game game = persistence.getPlayerGame().get(nickname);
            persistenceWaitingList.get(game).add(scc);

            HumanPlayer humanPlayer;

            for(Player player: game.getPlayers()){
                if(player.getNickName().equals(nickname) && !player.getNickName().equals("LorenzoIlMagnifico")){
                    humanPlayer = (HumanPlayer) player;
                    playersInGamePersistence.put(scc,humanPlayer);
                    sccRelateToPlayerPersistence.put(humanPlayer,scc);
                    player.setPlaying(true);
                    break;
                }
            }



            boolean lorenzo = false;
            for (Player player: game.getPlayers()) {
                if(player.getNickName().equals("LorenzoIlMagnifico")){
                    lorenzo=true;
                    break;
                }
            }

            if(persistenceWaitingList.get(game).size() == game.getPlayers().size() || (persistenceWaitingList.get(game).size() == 1 && lorenzo)){

                game.setInPause(false);
                GameHandler gameHandler = new GameHandler(sccRelateToPlayerPersistence, playersInGamePersistence, game, lorenzo);

                if(lorenzo) games.put(persistenceWaitingList.get(game).get(0), gameHandler);
                else {
                    for (int i = 0; i < game.getPlayers().size(); i++) {
                        games.put(persistenceWaitingList.get(game).get(i), gameHandler);
                    }
                }

                for (ServerClientConnection serverClientConnection: playersInGamePersistence.keySet()) {
                    serverClientConnection.setGameHandler(gameHandler);
                }

                gameHandler.initializationView();

            }
        }catch (Exception e){
            e.printStackTrace();}
    }

    public int getPort(){ return port; }

    public List<ServerClientConnection> getQueue(){ return queue; }

    public List<ServerClientConnection> getLobby4players() {
        return lobby4players;
    }

    public List<ServerClientConnection> getLobby3players() {
        return lobby3players;
    }

    public List<ServerClientConnection> getLobby2players() {
        return lobby2players;
    }

    public Persistence getPersistence() {
        return persistence;
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

    public static void main(String[] args){
        System.out.println(Constants.ANSI_RED + "Master of Renaissance | Server" + Constants.ANSI_RESET);
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





}
