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

public class GameHandler implements Serializable {

    private int numberOfPlayers;
    private Game game; //the instance of the game
    private HashMap<HumanPlayer, VirtualView> playerSockets;
    private HashMap<ServerClientConnection, HumanPlayer> playersInGame;
    private HashMap<HumanPlayer,ServerClientConnection> sccRelateToPlayer;
    private InitializationHandler initializationHandler;

    private Player firstPlayer;

    private TurnHandler turnHandler;

    public GameHandler(int numberOfPlayers, ArrayList<ServerClientConnection> playerSockets){

        ArrayList<Player> playersForGame = new ArrayList<>();
        initializationHandler = new InitializationHandler();
        this.playerSockets = new HashMap<>();
        playersInGame = new HashMap<>();
        sccRelateToPlayer = new HashMap<>();
        System.out.println("gioco da " + numberOfPlayers + "giocatori iniziato: ");

        boolean first = true;
        for (ServerClientConnection scc : playerSockets) {
            System.out.println(scc.getNickname());

            //creo un player per ogni scc
            HumanPlayer hPlayer = new HumanPlayer(scc.getNickname(), first);
            playersInGame.put(scc, hPlayer);
            sccRelateToPlayer.put(hPlayer,scc);
            playersForGame.add(hPlayer);
            if (first) firstPlayer=hPlayer;
            first = false;

            //metto a tutti gli scc il game handler di cui fanno parte
            scc.setGameHandler(this);
        }

        if(playersForGame.size() == 1){
            playersForGame.add(new LorenzoPlayer(playersForGame.get(0).getPopeTrack(), playersForGame.get(0).getDashboard()));
        }

        //Create the game
        game = new Game(playersForGame);

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

    public void handleInitialResourcesSettings(){

        ArrayList<ServerClientConnection> playerSockets = new ArrayList<>();
        for(ServerClientConnection scc : playersInGame.keySet()){
            playerSockets.add(scc);
        }

        for(ServerClientConnection scc : playerSockets){
            ArrayList<Resource> resources = initializationHandler.prepareInitialResources(getPlayersInGame().get(scc));
            scc.send(new InitialResourcesMessage("Initial resources" , resources));
            //The following message is useless
            //scc.send(new StartGameMessage("Start"));
        }
    }

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

    public void initializationView(){
        for(Player player: playersInGame.values()){
            VirtualView virtualView = new VirtualView(sccRelateToPlayer.get(player),game.getMarket(),game.getEvolutionSection(),player.getDashboard());
            playerSockets.put((HumanPlayer) player, virtualView);
        }

        //do ad ogni player la virtual view di tutti e dovrei registrarli come listeners
        for(Player player: playersInGame.values()){
            HashMap<HumanPlayer,VirtualView> otherPlayers = (HashMap<HumanPlayer, VirtualView>) playerSockets.clone();
            VirtualView myVirtuaView = otherPlayers.remove(player);
            playerSockets.get(player).setOtherPlayersView(otherPlayers);
            for (VirtualView virtualView: otherPlayers.values()){
                virtualView.addVirtualViewListener(myVirtuaView);
            }
            View view = createView(playerSockets.get(player));
            sccRelateToPlayer.get(player).send(new SendViewMessage("View",view));
            System.out.println("inviato il messaggio per: " + player.getNickName());
            sccRelateToPlayer.get(player).setGamePhase(GamePhases.GAME);
        }

        //a tutti devo dire che è cambiata la loro fare di gioco nell'scc
        for (ServerClientConnection scc : playersInGame.keySet()){
            scc.setGamePhase(GamePhases.GAME);
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

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public InitializationHandler getInitializationHandler() {
        return initializationHandler;
    }

    public void endGame(ServerClientConnection scc){
        synchronized (this){
            scc.setGamePhase(GamePhases.ENDGAME);
            scc.setActive(false);
            try {
                scc.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error while closing the socket");
            }
            scc.getServer().removeTakeNickname(scc.getNickname());

            //removing all the inactive players' nickname from the taken niknames
            for (ServerClientConnection serverClientConnection: playersInGame.keySet()){
                if(!scc.equals(serverClientConnection)){
                    if(!serverClientConnection.isActive()){
                        scc.getServer().removeTakeNickname(serverClientConnection.getNickname());
                    }
                }
            }
        }

    }

}
