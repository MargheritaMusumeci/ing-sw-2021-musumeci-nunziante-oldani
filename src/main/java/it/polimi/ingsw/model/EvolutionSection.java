package it.polimi.ingsw.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class EvolutionSection {

    private ArrayList<EvolutionCard>[][] evolutionSection;

    /**
     * constructor that creates the section in which EvolutionCards are stored and instantiates every card
     */
    public EvolutionSection(){

        evolutionSection = new ArrayList[3][4];

        for (int i=0; i<evolutionSection.length; i++){
            for(int j=0; j<evolutionSection[i].length; j++){
                evolutionSection[i][j] = new ArrayList<EvolutionCard>();

            }
        }

        populateSection();

    }

    /**
     * method that creates the instances for every card
     */
    private void populateSection(){

        int row = 0;
        int col = 0;

        EvolutionCard evolutionCard;
        CardColor c;
        LevelEnum l;

        File productionCard = new File("resouces/productionCard.txt");
        try {
            Scanner myScanner = new Scanner(productionCard);

            while (myScanner.hasNextLine()){
                String data = myScanner.nextLine();

                if(data.charAt(0) == 'n'){
                    int position = data.charAt(1) - '0';
                    int level = Integer.parseInt(myScanner.nextLine());
                    char color = myScanner.nextLine().charAt(0);
                    String cost = myScanner.nextLine();
                    String requirements = myScanner.nextLine();
                    String products = myScanner.nextLine();
                    int gamePoints = Integer.parseInt(myScanner.nextLine());

                    switch (level){
                        case 1: l = LevelEnum.FIRST;
                                break;
                        case 2: l = LevelEnum.SECOND;
                            break;
                        case 3: l = LevelEnum.THIRD;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + level);
                    }

                    switch (color){
                        case 'g': c = CardColor.GREEN;
                            break;
                        case 'y': c = CardColor.YELLOW;
                            break;
                        case 'b': c = CardColor.BLUE;
                            break;
                        case 'p': c = CardColor.PURPLE;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + color);
                    }

                    if(position == 1){
                        evolutionSection[row][col] = new ArrayList<>();
                        evolutionCard = new EvolutionCard(c, l, gamePoints, processCost(cost), processRequirements(requirements), processProducts(products));
                        evolutionSection[row][col].add(evolutionCard);
                    }else{
                        evolutionCard = new EvolutionCard(c, l, gamePoints, processCost(cost), processRequirements(requirements), processProducts(products));
                        evolutionSection[row][col].add(evolutionCard);
                        if(position == 4){
                            if(col < 3){
                                col++;
                            }else{
                                col = 0;
                                row++;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * @return the fist card of each section that can be bought, if the cards in that postion are finished
     * the position is set to null
     */
    public EvolutionCard[][] canBuy(){

        EvolutionCard[][] c = new EvolutionCard[3][4];
        for (int i=0; i<evolutionSection.length; i++){
            for(int j=0; j<evolutionSection[i].length; j++){
                if(evolutionSection[i][j].size() > 0){
                    c[i][j] = evolutionSection[i][j].get(0);
                }else{
                    c[i][j] = null;
                }
            }
        }

        return c;
    }

    /**
     *
     * @param row indicates the row in which the card to buy is
     * @param col indicates the row in which the card to buy is
     * @return the card bought
     */
    public EvolutionCard buy(int row, int col){
        EvolutionCard c = evolutionSection[row][col].get(0);
        evolutionSection[row][col].remove(0);
        return c;
    }


    /**
     * method that process the string with the cost read from the file
     * @param cost is the string to be processed
     * @return the array that corresponds to the string
     */
    private int[] processCost(String cost){
        int[] costArray = new int[4];

        costArray[0] = cost.charAt(0) - '0';
        costArray[1] = cost.charAt(1) - '0';
        costArray[2] = cost.charAt(2) - '0';
        costArray[3] = cost.charAt(3) - '0';

        return costArray;
    }

    /**
     * method that process the string with the requirements read from the file
     * @param requirements is the string to be processed
     * @return the array that corresponds to the string
     */
    private int[] processRequirements(String requirements){
        int[] reqArray = new int[4];

        reqArray[0] = requirements.charAt(0) - '0';
        reqArray[1] = requirements.charAt(1) - '0';
        reqArray[2] = requirements.charAt(2) - '0';
        reqArray[3] = requirements.charAt(3) - '0';

        return reqArray;
    }

    /**
     * method that process the string with the products read from the file
     * @param products is the string to be processed
     * @return the array that corresponds to the string
     */
    private int[] processProducts(String products){
        int[] prodArray = new int[4];

        prodArray[0] = products.charAt(0) - '0';
        prodArray[1] = products.charAt(1) - '0';
        prodArray[2] = products.charAt(2) - '0';
        prodArray[3] = products.charAt(3) - '0';
        prodArray[4] = products.charAt(4) - '0';

        return prodArray;
    }




}
