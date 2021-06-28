package it.polimi.ingsw.server;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.board.LeaderProductionZone;
import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.model.popeTrack.PopeTrack;
import it.polimi.ingsw.serializableModel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

// se il server cade invio un game aborted ?

public class Persistence {
    Server server;
    HashMap<String, Game> playerGame;

    public Persistence(Server server) {
        this.server = server;
        playerGame = new HashMap<>();
    }


    public PersistenceSerializableGame readGame(String path) {

        PersistenceSerializableGame game = null;

        try {
            FileInputStream file = new FileInputStream(path);
            System.out.println("path in readGame: " + path);
            ObjectInputStream streamer = new ObjectInputStream(file);

            game = (PersistenceSerializableGame) streamer.readObject();
            streamer.close();

            System.out.println("Done");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return game;
    }

    public void saveGame(Game game) {
        PersistenceSerializableGame persistenceSerializableGame = new PersistenceSerializableGame(game);
        try {

            String path = "";
            for (Player player : game.getPlayers()) {
                path = path + player.getNickName();
            }
            FileOutputStream file = new FileOutputStream("C:\\Users\\margh\\IdeaProjects\\ing-sw-2021-musumeci-nunziante-oldani\\src\\main\\resources\\savedGames\\" + path + ".ser");
            ObjectOutputStream streamer = new ObjectOutputStream(file);

            // write object to file
            streamer.writeObject(persistenceSerializableGame);
            System.out.println("Done");

            // closing resources
            streamer.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteGame(String absolutePath) {
        File file = new File(absolutePath);
        file.delete();
    }


    public void initializeGame() {

        try {
            File[] files = new File("C:\\Users\\margh\\IdeaProjects\\ing-sw-2021-musumeci-nunziante-oldani\\src\\main\\resources\\savedGames").listFiles();

            if (files == null) {
                server.setPersistenceWaitingList(null);
                return;
            }

            for (File file : files) {
                PersistenceSerializableGame persistenceSerializableGame = readGame(file.getAbsolutePath());
                System.out.println(persistenceSerializableGame.getActivePlayerNickname());

                Game game = recreateGameFromPersistence(persistenceSerializableGame);

                server.getPersistenceWaitingList().put(game, new ArrayList<>());

                for (Player player : game.getPlayers()) {
                    if(!player.getNickName().equals("LorenzoIlMagnifico")) {
                        server.getPersistenceNicknameList().add(player.getNickName());
                        playerGame.put(player.getNickName(), game);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Game recreateGameFromPersistence(PersistenceSerializableGame persistenceSerializableGame) {

        try {
            Player activePlayer = null;
            Market market = new Market();
            market.setMarketBoard(persistenceSerializableGame.getMarket().getMarket(), persistenceSerializableGame.getMarket().getExternalResource());

            EvolutionSection evolutionSection = new EvolutionSection();
            evolutionSection.setEvolutionSection(persistenceSerializableGame.getEvolutionSection());

            //devo creare i player
            ArrayList<Player> players = new ArrayList<>();
            for (String playerNickname : persistenceSerializableGame.getPlayerNicknames()) {

                if (!playerNickname.equals("LorenzoIlMagnifico")) {

                    boolean inkwell;
                    if (playerNickname.equals(persistenceSerializableGame.getPlayerNicknames().get(0))) {
                        inkwell = true;
                    } else {
                        inkwell = false;
                    }

                    SerializableStock stock = persistenceSerializableGame.getPlayerDashboards().get(playerNickname).getSerializableStock();
                    SerializableLockBox lockBox = persistenceSerializableGame.getPlayerDashboards().get(playerNickname).getSerializableLockBox();

                    SerializableProductionZone[] normalProductionZones = persistenceSerializableGame.getPlayerDashboards().get(playerNickname).getSerializableProductionZones();
                    ArrayList<LeaderProductionZone> leaderProductionZone = persistenceSerializableGame.getLeaderProductionZoneHashMap().get(playerNickname);

                    PopeTrack popeTrack = persistenceSerializableGame.getPopeTrackHashMap().get(playerNickname);

                    ArrayList<LeaderCard> leaderCards = persistenceSerializableGame.getLeaderCards().get(playerNickname);

                    Dashboard dashboard = new Dashboard(playerNickname, inkwell, popeTrack, leaderCards, stock, lockBox, normalProductionZones, leaderProductionZone);
                    Player player = new HumanPlayer(playerNickname, popeTrack, dashboard);
                    players.add(player);
                    if (playerNickname.equals(persistenceSerializableGame.getActivePlayerNickname()))
                        activePlayer = player;

                    //se è un solo game ricostruisco anche lorenzo
                    if(persistenceSerializableGame.getPlayerNicknames().contains("LorenzoIlMagnifico")){
                        players.add(new LorenzoPlayer(popeTrack,dashboard,true));
                    }
                }
            }
            return new Game(players, market, evolutionSection, activePlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HashMap<String, Game> getPlayerGame() {
        return playerGame;
    }

    public void setPlayerGame(HashMap<String, Game> playerGame) {
        this.playerGame = playerGame;
    }
}
