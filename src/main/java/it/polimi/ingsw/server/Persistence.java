package it.polimi.ingsw.server;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;
import it.polimi.ingsw.serializableModel.PersistenceSerializableGame;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Persistence {
    Server server;

    public Persistence(Server server){
        this.server=server;
    }


    public PersistenceSerializableGame readGame(String path){

        //per ogni file presente della cartella
        //dopo che ho ricreato il gioco posso effettuare una riconnessione del client?
        PersistenceSerializableGame game = null;

        try{
            FileInputStream file = new FileInputStream(path);
            System.out.println("path in readGame: " + path);
            ObjectInputStream streamer = new ObjectInputStream(file);

            game = (PersistenceSerializableGame) streamer.readObject();
            streamer.close();

            System.out.println("Done");

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return game;

        //dove metto il metodo per ristabilire il resto delle classi?
    }



    public void saveGame(Game game){
        PersistenceSerializableGame persistenceSerializableGame = new PersistenceSerializableGame(game);
        try {

            String path ="";
            for (Player player :game.getPlayers()) {
                path = path + player.getNickName();
            }
            FileOutputStream file = new FileOutputStream("/Users/matteoldani/IdeaProjects/ing-sw-2021-musumeci-nunziante-oldani/src/main/resources/SevedGames/" + path + ".ser");
            ObjectOutputStream streamer = new ObjectOutputStream(file);

            // write object to file
            streamer.writeObject(persistenceSerializableGame);
            System.out.println("Done");

            // closing resources
            streamer.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    public void deleteGame(ServerClientConnection scc){
        //in base all'scc
        File file = new File("../../../../../resources/MastersOfRenaissance.ser");
        file.delete();
    }

    public void restartGame(){
        //dal nome del file prendo l'scc
        ServerClientConnection scc;

    }


     */

    public void initializeGame() {

        try{
            File[] files = new File("/Users/matteoldani/IdeaProjects/ing-sw-2021-musumeci-nunziante-oldani/src/main/resources/SevedGames").listFiles();

            if(files == null ) {
                server.setPersistenceWaitingList(null);
                return;
            }

            for (File file: files) {
                PersistenceSerializableGame persistenceSerializableGame = readGame(file.getPath());
                System.out.println(persistenceSerializableGame.getActivePlayerNickname());
           /*
           server.getPersistenceWaitingList().put(game,new ArrayList<>());

            for (Player player: game.getPlayers()) {
                server.getPersistenceNicknameList().add(player.getNickName());
            }

            */
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Game recreateGameFromPersistence(PersistenceSerializableGame persistenceSerializableGame){
        Market market = new Market();
        market.setMarketBoard(persistenceSerializableGame.getMarket().getMarket(), persistenceSerializableGame.getMarket().getExternalResource());
        EvolutionSection evolutionSection = new EvolutionSection();
        evolutionSection.setEvolutionSection(persistenceSerializableGame.getEvolutionSection());

        //devo creare i player
        ArrayList<Player> players = new ArrayList<>();
        for(String playerNickname: persistenceSerializableGame.getPlayerNicknames()){

            if(playerNickname.equals("LorenzoIlMagnifico")){

                players.add(new LorenzoPlayer(players.get(0).getPopeTrack(), players.get(0).getDashboard()));
            }else{
                boolean inkwell;
                if(playerNickname.equals(persistenceSerializableGame.getPlayerNicknames().get(0))){
                    inkwell = true;
                }else{
                    inkwell = false;
                }
                players.add(new HumanPlayer(playerNickname, inkwell));

                Dashboard dashboard = new Dashboard(playerNickname, inkwell, personaLpopetrack);
            }
        }
        Game game = new Game();

        return game;
    }



}
