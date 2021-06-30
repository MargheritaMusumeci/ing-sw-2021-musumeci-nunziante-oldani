package it.polimi.ingsw.server;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.InitializationHandler;
import it.polimi.ingsw.controller.TurnHandler;
import it.polimi.ingsw.controller.TurnHandlerMultiPlayer;
import it.polimi.ingsw.controller.TurnHandlerSoloGame;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.FourLeaderCardsMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.InitialResourcesMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.MarketAndEvolutionSectionMessage;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.SendViewMessage;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.serializableModel.SerializableDashboard;
import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.serializableModel.SerializableMarket;
import it.polimi.ingsw.server.virtualView.VirtualView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * class that orchestrate the game server side, it will create the virtual view for all the players. It
 * also handles the end of the game. There is a gameHandler for every game
 */
public class GameHandler implements Serializable {

    private int numberOfPlayers;
    private Game game; //the instance of the game
    private HashMap<HumanPlayer, VirtualView> playerSockets;
    private HashMap<ServerClientConnection, HumanPlayer> playersInGame;
    private HashMap<HumanPlayer,ServerClientConnection> sccRelateToPlayer;
    private InitializationHandler initializationHandler;

    private Player firstPlayer;

    private TurnHandler turnHandler;

    /**
     * class constructor that creates a gameHandler based on a game reloaded thanks for persistence
     * @param sccRelateToPlayerPersistence is an hashmap that puts in relation the player with its connection
     * @param playersInGamePersistence  is an hashmap that puts in relation the connection with its player
     * @param game is the game that has been restored
     * @param soloGame is true if its a solo game
     */
    public GameHandler(HashMap<HumanPlayer,ServerClientConnection> sccRelateToPlayerPersistence, HashMap<ServerClientConnection,
            HumanPlayer> playersInGamePersistence, Game game, boolean soloGame){

        this.game = game;
        if (soloGame)
        {
            numberOfPlayers = 1;
            turnHandler = new TurnHandlerSoloGame(game);
        }
        else {
            numberOfPlayers = game.getPlayers().size();
            turnHandler = new TurnHandlerMultiPlayer(game);
        }

        playersInGame = playersInGamePersistence;
        sccRelateToPlayer = sccRelateToPlayerPersistence;
        playerSockets = new HashMap<>();
    }

    /**
     * class normal constructor. It handles the creation of the game and instantiates the correct turnHandler based on
     * the number of players
     * @param numberOfPlayers is the number of player in the game
     * @param playerSockets contains the connection with the clients belonging to this game
     */
    public GameHandler(int numberOfPlayers, ArrayList<ServerClientConnection> playerSockets){

        ArrayList<Player> playersForGame = new ArrayList<>();
        initializationHandler = new InitializationHandler();
        this.playerSockets = new HashMap<>();
        playersInGame = new HashMap<>();
        sccRelateToPlayer = new HashMap<>();

        boolean first = true;
        for (ServerClientConnection scc : playerSockets) {
            System.out.println(scc.getNickname());

            //create a player for every scc
            HumanPlayer hPlayer = new HumanPlayer(scc.getNickname(), first);
            playersInGame.put(scc, hPlayer);
            sccRelateToPlayer.put(hPlayer,scc);
            playersForGame.add(hPlayer);
            if (first) firstPlayer=hPlayer;
            first = false;

            //add gamehandler to scc
            scc.setGameHandler(this);
        }

        if(playersForGame.size() == 1){
            playersForGame.add(new LorenzoPlayer(playersForGame.get(0).getPopeTrack(), playersForGame.get(0).getDashboard()));
        }

        //Create the game
        game = new Game(playersForGame);


        //Send market and evolution section before the game is started to make player see it while choosing the leader cards
        SerializableMarket serializableMarket = new SerializableMarket(game.getMarket());
        SerializableEvolutionSection serializableEvolutionSection = new SerializableEvolutionSection(game.getEvolutionSection(), null);
        //TODO controllare che nella serializable evolution section serva veramente il player
        for(ServerClientConnection scc : playerSockets) {
            ArrayList<SerializableLeaderCard> serializableLeaderCards = scc.getGameHandler().getInitializationHandler().takeLeaderCards(scc.getGameHandler().getPlayersInGame().get(scc));
            scc.send(new MarketAndEvolutionSectionMessage("market and evolution section", serializableMarket, serializableEvolutionSection));
            scc.send(new FourLeaderCardsMessage("4 Leader card", serializableLeaderCards));
        }

        this.numberOfPlayers = numberOfPlayers;


        if(numberOfPlayers == 1){
            turnHandler = new TurnHandlerSoloGame(game);
        }else{
            turnHandler = new TurnHandlerMultiPlayer(game);
        }

    }

    /**
     * method that prepares the initials resources and send it to clients
     */
    public void handleInitialResourcesSettings(){

        ArrayList<ServerClientConnection> playerSockets = new ArrayList<>();
        for(ServerClientConnection scc : playersInGame.keySet()){
            playerSockets.add(scc);
        }

        for(ServerClientConnection scc : playerSockets){
            ArrayList<Resource> resources = initializationHandler.prepareInitialResources(getPlayersInGame().get(scc));
            scc.send(new InitialResourcesMessage("Initial resources" , resources));
        }
    }

    /**
     * method able to create a view from a virtual view.
     * It is also responsible to create "otherPlayersView"
     * @param virtualView is the virtual view from which create the view
     * @return the View to be sent
     */
    public View createView(VirtualView virtualView){
        SerializableDashboard serializableDashboard = new SerializableDashboard(virtualView.getPersonalDashboard());
        SerializableMarket serializableMarket = new SerializableMarket(virtualView.getMarket());
        SerializableEvolutionSection serializableEvolutionSection = new SerializableEvolutionSection(virtualView.getEvolutionSection(), playersInGame.get(virtualView.getScc()));
        ArrayList<SerializableLeaderCard> serializableLeaderCards = new ArrayList<>();

        for(int i = 0; i<virtualView.getPersonalDashboard().getLeaderCards().size();i++) {
            SerializableLeaderCard serializableLeaderCard = new SerializableLeaderCard(virtualView.getPersonalDashboard().getLeaderCards().get(i));
            serializableLeaderCards.add(serializableLeaderCard);
        }

        HashMap<String, SerializableDashboard> serializableDashboards = new HashMap<>();

        for(Player player: virtualView.getOtherPlayersView().keySet()) {
            SerializableDashboard serializableDashboardEnemy = new SerializableDashboard(virtualView.getOtherPlayersView().get(player).getPersonalDashboard());
            serializableDashboards.put(player.getNickName(), serializableDashboardEnemy);
        }



        View view = new View(game.getActivePlayer().getNickName(),virtualView.getScc().getNickname(),
                serializableDashboard,serializableDashboards,serializableMarket,serializableEvolutionSection,
                serializableLeaderCards, virtualView.getPersonalDashboard().getScore());

        return view;
    }

    /**
     * method that creates the virtual view during the game initialization and send it to the
     * correct player
     */
    public void initializationView(){
        for(Player player: playersInGame.values()){
            VirtualView virtualView = new VirtualView(sccRelateToPlayer.get(player),game.getMarket(),game.getEvolutionSection(),player.getDashboard());
            playerSockets.put((HumanPlayer) player, virtualView);
        }

        //Create the hashmap with the view of the other players
        for(Player player: playersInGame.values()){
            HashMap<HumanPlayer,VirtualView> otherPlayers = (HashMap<HumanPlayer, VirtualView>) playerSockets.clone();
            VirtualView myVirtuaView = otherPlayers.remove(player);
            playerSockets.get(player).setOtherPlayersView(otherPlayers);
            for (VirtualView virtualView: otherPlayers.values()){
                virtualView.addVirtualViewListener(myVirtuaView);
            }
            View view = createView(playerSockets.get(player));
            sccRelateToPlayer.get(player).send(new SendViewMessage("View",view));
            sccRelateToPlayer.get(player).setGamePhase(GamePhases.GAME);
        }

        //Set the new game phase for every player
        for (ServerClientConnection scc : playersInGame.keySet()){
            scc.setGamePhase(GamePhases.GAME);
        }
    }

    /**
     * method that handles the end of the game. It will remove the file with the game state used for persistence.
     * It will also close the connections with the sockets and remove the nicknames from the list of taken ones in the
     * server
     * @param scc is the scc that called this method
     */
    public void endGame(ServerClientConnection scc){


        synchronized (this){
            scc.setGamePhase(GamePhases.ENDGAME);
            scc.setActive(false);

            //delete persistence file
            String path ="";
            for (Player player : game.getPlayers()) {
                path = path + player.getNickName();
            }
            scc.getServer().getPersistence().deleteGame(System.getProperty("java.io.tmpdir") + "/savedGames/" + path + ".ser");


            try {
                scc.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error while closing the socket");
            }


            scc.getServer().removeTakeNickname(scc.getNickname());

            //removing all the inactive players' nickname from the taken nicknames
            for (ServerClientConnection serverClientConnection: playersInGame.keySet()){
                if(!scc.equals(serverClientConnection)){

                    scc.getServer().removeTakeNickname(serverClientConnection.getNickname());
                }
            }
        }

    }

    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Game getGame() {
        return game;
    }

    public HashMap<HumanPlayer, VirtualView> getPlayerSockets() {
        return playerSockets;
    }

    public HashMap<ServerClientConnection, HumanPlayer> getPlayersInGame() {
        return playersInGame;
    }

    public InitializationHandler getInitializationHandler() {
        return initializationHandler;
    }



}
