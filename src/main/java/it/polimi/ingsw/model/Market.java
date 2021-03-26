package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Market {

    private Resource[][] marketBoard;
    private Resource externalResource;

    /**
     * initialize a marker with random resource positions and random external resource according to the rules
     * the number of type of resources is defined, only the position is random
     */
    public Market(){
        marketBoard = new Resource[3][4];
        randomPopulateBoard();
    }

    /**
     * method that really populate the board when the object is istantiated
     */
    private void randomPopulateBoard(){
        ArrayList<ResourceType> listOfElement = new ArrayList<ResourceType>();
        Random r = new Random();
        int numChosen;

        //2 coins, 2 shilds, 2 servants, 2 rocks, 1 faith, 4 nothing --> tot 13
        listOfElement.add(ResourceType.COIN);
        listOfElement.add(ResourceType.COIN);
        listOfElement.add(ResourceType.ROCK);
        listOfElement.add(ResourceType.ROCK);
        listOfElement.add(ResourceType.SERVANT);
        listOfElement.add(ResourceType.SERVANT);
        listOfElement.add(ResourceType.SHIELD);
        listOfElement.add(ResourceType.SHIELD);
        listOfElement.add(ResourceType.FAITH);
        listOfElement.add(ResourceType.NOTHING);
        listOfElement.add(ResourceType.NOTHING);
        listOfElement.add(ResourceType.NOTHING);
        listOfElement.add(ResourceType.NOTHING);

        numChosen = r.nextInt(listOfElement.size());
        externalResource = new Resource(listOfElement.get(numChosen));
        listOfElement.remove(numChosen);

        for (int i=0; i<marketBoard.length; i++){
            for (int j=0; j<marketBoard[i].length;j++){
                if(listOfElement.size() != 0){
                    numChosen = r.nextInt(listOfElement.size());
                }else{
                    numChosen = 0;
                }

                marketBoard[i][j] = new Resource(listOfElement.get(numChosen));
                listOfElement.remove(numChosen);

            }
        }

    }

    /**
     *
     * @param position is the index of the row/column chosen to play the move in the market
     * @param isRow if true the player have chosen a row, if false a column
     */
    public void updateBoard(int position, boolean isRow){

        Resource tempRes;

        if(isRow){
            tempRes = marketBoard[position][0];
            for (int i=0; i< marketBoard[position].length - 1; i++){
                marketBoard[position][i] = marketBoard[position][i+1];
            }
            marketBoard[position][3] = externalResource;
            externalResource = tempRes;
        }else{
            tempRes = marketBoard[0][position];
            for (int i=0; i< marketBoard.length - 1; i++){
                marketBoard[i][position] = marketBoard[i+1][position];
            }
            marketBoard[2][position] = externalResource;
            externalResource = tempRes;
        }
    }

    /**
     * method that return the resource at the given position in the market
     * @param row is the number of the chosen row (0 is the first)
     * @param column is the number of the chosen column (0 is the first)
     * @return the resource at that position in the market
     */
    public Resource getPosition(int row, int column){
        return new Resource(marketBoard[row][column].getType());
    }
}
