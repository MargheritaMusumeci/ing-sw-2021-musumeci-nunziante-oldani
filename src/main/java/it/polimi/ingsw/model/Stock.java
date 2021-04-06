package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.NotEnoughResourcesException;
import it.polimi.ingsw.exception.NotEnoughSpaceException;
import it.polimi.ingsw.exception.ResourceAlreadyPresentException;

import java.util.ArrayList;
import java.util.Arrays;

public class Stock {
    private ArrayList<Resource[]> boxes;

    /**
     * Initialize an empty stock
     */
    public Stock(){
        boxes = new ArrayList<Resource[]>();
        Resource[] box0 = new Resource[1];
        Resource[] box1 = new Resource[2];
        Resource[] box2 = new Resource[3];
        Arrays.fill(box0 , null);
        Arrays.fill(box1 , null);
        Arrays.fill(box2 , null);
        boxes.add(box0);
        boxes.add(box1);
        boxes.add(box2);
    }

    /**
     * Assumption : the box was filled starting from the first position
     *
     * @param originBox number of box I want the resource type
     * @return the ResourceType of the required box
     */
    public Resource getResourceType(int originBox){
        return boxes.get(originBox)[0];
    }

    /**
     * Add resources in the specified origin box starting from the first free position
     * @param originBox box I would like to insert the resources
     * @param numberResources number of resources I would like to insert
     * @param resourceType type of the resources I'm adding
     * @throws NotEnoughSpaceException if there isn't enough space in the originBox
     * @throws ResourceAlreadyPresentException if the resource type specified is already in an other box
     */
    public void addResources(int originBox , int numberResources , Resource resourceType) throws NotEnoughSpaceException, ResourceAlreadyPresentException {

        for(int i = 0; i < boxes.size(); i++){
            if(i != originBox){
                if(resourceType == getResourceType(originBox)) throw new ResourceAlreadyPresentException("The resource type is already in an other box");
            }
            else{
                if(boxes.get(originBox).length - getQuantities(originBox) < numberResources) throw new NotEnoughSpaceException("The box is too small");
            }
        }

        Resource[] box = boxes.get(originBox);
        int i = 0;
        int maxDim = box.length;

        while(numberResources != 0 && i < maxDim){
            if(box[i] == null){
                box[i] = resourceType;
                numberResources--;
            }
            i++;
        }
        boxes.set(originBox , box);
    }

    /**
     * @param originBox number of box we are interested
     * @return the quantity of resources in the box specified by originBox
     */
    public int getQuantities(int originBox){
        int quantity = 0;
        int i = 0;
        int maxDim = boxes.get(originBox).length;

        while(i < maxDim){
            if(boxes.get(originBox)[i] != null)
                quantity++;
            i++;
        }
        return quantity;
    }

    /**
     *
     * @param originBox
     * @return the length of the originBox
     */
    public int getBoxLength(int originBox){ return boxes.get(originBox).length; }

    /**
     * Eliminate resources from the last position to the first position
     * @param originBox number of box we are interested
     * @param numberResources number of resources we use
     * @throws NotEnoughResourcesException if the number of the resources required is smaller than the number of available resources
     */
    public void useResources(int originBox , int numberResources) throws NotEnoughResourcesException{

        if(numberResources > getQuantities(originBox)) throw new NotEnoughResourcesException("You don't have enough resources");

        Resource[] box = boxes.get(originBox);
        int i  = box.length;

        while(numberResources != 0)
        {
            if(box[i - 1] != null){
                box[i - 1] = null;
                numberResources--;
            }
            i--;
        }
        boxes.set(originBox , box);
    }

    /**
     *
     * @param originBox from where take the resources
     * @param destinationBox where put the resources
     * @return true if the operation is completed, false otherwise
     * @throw NotEnoughSpaceException if one of the box is too smaller to contain the amount of resources in the other box
     */
    public void moveResources(int originBox , int destinationBox) throws NotEnoughSpaceException {
        if(getQuantities(originBox) > getBoxLength(destinationBox)) throw new NotEnoughSpaceException("Destination box is too small");
        if(getQuantities(destinationBox) > getBoxLength(originBox)) throw new NotEnoughSpaceException("Origin box is too small");

        Resource[] boxOrigin = boxes.get(originBox);
        Resource[] boxDestination = boxes.get(destinationBox);
        Arrays.fill(boxOrigin , null);
        Arrays.fill(boxDestination , null);
        int originQuantities = getQuantities(originBox);
        int destinationQuantities = getQuantities(destinationBox);
        for(int i = 0; i < originQuantities; i++){
            boxDestination[i] = boxes.get(originBox)[i];
        }
        for(int j = 0; j < destinationQuantities; j++){
            boxOrigin[j] = boxes.get(destinationBox)[j];
        }
        boxes.set(originBox , boxOrigin);
        boxes.set(destinationBox , boxDestination);
    }

    /**
     * add into boxes a new box of variable dimension in case of special ability
     * @param howBig dimension of the new box
     */
    public void addBox(int howBig){
        Resource[] newBox = new Resource[howBig];
        boxes.add(newBox);
    }
}
