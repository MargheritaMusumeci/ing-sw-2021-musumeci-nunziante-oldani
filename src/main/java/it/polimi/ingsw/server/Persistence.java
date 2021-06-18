package it.polimi.ingsw.server;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.Player;

import java.io.*;
import java.util.ArrayList;

public class Persistence {
    Server server;

    public Persistence(Server server){
        this.server=server;
    }
/*
    public Game readGame(String path){

        //per ogni file presente della cartella
        //dopo che ho ricreato il gioco posso effettuare una riconnessione del client?
        Game game = null;

        try{
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream streamer = new ObjectInputStream(file);

            game = (Game) streamer.readObject();
            streamer.close();

            System.out.println("Done");

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return game;

        //dove metto il metodo per ristabilire il resto delle classi?
    }

    public void saveGame(Game game){

        try {

            String path ="";
            for (Player player :game.getPlayers()) {
                path = path + player.getNickName();
            }
            FileOutputStream file = new FileOutputStream("C:\\Users\\margh\\IdeaProjects\\ing-sw-2021-musumeci-nunziante-oldani\\src\\main\\resources\\persistenceFile\\" + path + ".ser");
            ObjectOutputStream streamer = new ObjectOutputStream(file);

            // write object to file
            streamer.writeObject(game);
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

    public void deleteGame(ServerClientConnection scc){
        //in base all'scc
        File file = new File("../../../../../resources/MastersOfRenaissance.ser");
        file.delete();
    }

    public void restartGame(){
        //dal nome del file prendo l'scc
        ServerClientConnection scc;

    }

    public void initializeGame() {

        File[] files = new File("C:\\Users\\margh\\IdeaProjects\\ing-sw-2021-musumeci-nunziante-oldani\\src\\main\\resources\\persistenceFile").listFiles();

        if(files == null ) {
            server.setPersistenceWaitingList(null);
            return;
        }

        for (File file: files) {
           Game game = readGame(file.getPath());
           server.getPersistenceWaitingList().put(game,new ArrayList<>());

            for (Player player: game.getPlayers()) {
                server.getPersistenceNicknameList().add(player.getNickName());
            }
        }
    }

 */
}
