package it.polimi.ingsw.controller;
import it.polimi.ingsw.exception.ExcessOfPositionException;
import it.polimi.ingsw.exception.NotEnoughSpaceException;
import it.polimi.ingsw.exception.OutOfBandException;
import it.polimi.ingsw.exception.ResourceAlreadyPresentException;
import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.Player;


public class doActionPlayer {

    private Game modelGame;


    public doActionPlayer(Game modelGame){
       this.modelGame=modelGame;
    }

    public void buyFromMarket(int position, boolean isRow) throws ExcessOfPositionException, OutOfBandException, ResourceAlreadyPresentException, NotEnoughSpaceException {

        //devo controllare che posso effettivamente svolgere la mossa?
        //if(  modelGame.getActivePlayer().getActionState()==false){}

        //mossa effettuata
        modelGame.getActivePlayer().setActionState(true);

        //prendo le risorse dal mercato e aggiorno qust'ultimo
        Resource[] resources = modelGame.getMarket().updateBoard(position,isRow);

        Stock stock = modelGame.getActivePlayer().getDashboard().getStock();
        //controllo che ci sia spazio per inserire ogni risorsa. In caso contrario incremento il pope track di ogni player.
        int freeSpaces=0;

        //boxPlus included
        for(int i=0;i< stock.getNumberOfBoxes();i++){
                freeSpaces= freeSpaces + stock.getBoxLength(i)-stock.getQuantities(i);
        }
        if (freeSpaces<resources.length){
            for (Player player:modelGame.getPlayers()){
                if(player.equals(modelGame.getActivePlayer())){
                    player.getPopeTrack().updateGamerPosition(freeSpaces);
                }
            }
        }

        int stockBox;

        //prendo dal client le informazioni riguardanti il tipo di risorsa e la futura locazione della risorsa
        for(int i=0;i<resources.length;i++) {
            stockBox = 1; //sarà il risultato di un metodo che chiede al client in che box inserire le nuove biglie
            stock.addResources(stockBox, 1, resources[i]);
        }
    }
}
