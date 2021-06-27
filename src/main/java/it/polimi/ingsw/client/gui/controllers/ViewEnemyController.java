package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewEnemyController extends ViewController{

    private String nickName;

    /**
     * Method that sets the enemy dashboard in the initializer and initialize the enemy view
     */
    @Override
    public void init(){
        initializer.setDashboard(gui.getView().getEnemiesDashboard().get(nickName));
        super.init();
        initLockBox();
        initStock();
        initLeaderCards();
    }

    /**
     * Method that initializes the lockBox with the amount of each resource
     */
    private void initLockBox() {
        HashMap<Resource, Integer> lockBox = gui.getView().getEnemiesDashboard().get(nickName).getSerializableLockBox().getResources();
        coinQuantity.setText(String.valueOf(lockBox.get(Resource.COIN)));
        shieldQuantity.setText(String.valueOf(lockBox.get(Resource.SHIELD)));
        servantQuantity.setText(String.valueOf(lockBox.get(Resource.SERVANT)));
        rockQuantity.setText(String.valueOf(lockBox.get(Resource.ROCK)));
    }

    /**
     * Method that initializes the standard stock and, if present, the stock plus
     */
    private void initStock() {

        ArrayList<Resource[]> boxes = gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxes();

        initStockCommon(boxes);

        ArrayList<SerializableLeaderCard> leaderCardsActivated =
                gui.getView().getEnemiesActivatedLeaderCards().get(gui.getView().getEnemiesDashboard().get(nickName));

        //Initialize leader stock
        if (leaderCardsActivated != null && leaderCardsActivated.size() != 0) {

            int currentLeaderCard = -1;
            int numberOfStockLeaderCard = -1;
            for(SerializableLeaderCard leaderCard : leaderCardsActivated){
                currentLeaderCard++;
                if(leaderCard.getAbilityType().equals(LeaderAbility.STOCKPLUS)){
                    numberOfStockLeaderCard++;
                    if (gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(numberOfStockLeaderCard).length != 0){
                        for(int j = 0 ; j < gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(numberOfStockLeaderCard).length ; j++){
                            if(gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(numberOfStockLeaderCard)[j] != null){
                                String path = printer.pathFromResource(gui.getView().getEnemiesDashboard().get(nickName).getSerializableStock().getBoxPlus().get(numberOfStockLeaderCard)[j]);
                                stockPlus.get(currentLeaderCard).get(j).setImage(printer.fromPathToImageResource(path));
                            }
                            else{
                                stockPlus.get(currentLeaderCard).get(j).setImage(null);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Method that initializes the enemy leader card: can see only the leader card activated by the enemy; show a close
     *      door otherwise.
     */
    private void initLeaderCards() {

        //set leader card
        ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getEnemiesActivatedLeaderCards().get(gui.getView().getEnemiesDashboard().get(nickName));
        if (leaderCards != null && leaderCards.size() > 0) {

            String path = String.valueOf(leaderCards.get(0).getId());
            leader1.setImage(printer.fromPathToImageLeader(path));

            //Reset this value -> otherwise nowhere it is reset
            if(!leaderCards.get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)){
                stockPlus.get(0).get(0).setImage(null);
                stockPlus.get(0).get(1).setImage(null);
            }

            if (leaderCards.size() > 1) {
                path = String.valueOf(leaderCards.get(1).getId());
                leader2.setImage(printer.fromPathToImageLeader(path));

                if(!leaderCards.get(1).getAbilityType().equals(LeaderAbility.STOCKPLUS)){
                    stockPlus.get(1).get(0).setImage(null);
                    stockPlus.get(1).get(1).setImage(null);
                }

            } else {
                leader2.setImage(printer.fromPathToImageLeader("back_door"));
            }
        } else {
            leader1.setImage(printer.fromPathToImageLeader("back_door"));
            leader2.setImage(printer.fromPathToImageLeader("back_door"));
        }
    }

    //GETTER AND SETTER
    public void setNickname(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

}