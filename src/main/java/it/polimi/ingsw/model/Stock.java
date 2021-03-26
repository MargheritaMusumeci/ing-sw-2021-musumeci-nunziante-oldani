package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Stock {
    private ArrayList<Resource[]> boxes;

    /**
     * Initialize a free stock
     */
    public Stock(){
        boxes = new ArrayList<Resource[]>();
        Resource[] box0 = new Resource[1];
        Resource[] box1 = new Resource[2];
        Resource[] box2 = new Resource[3];
        boxes.add(box0);
        boxes.add(box1);
        boxes.add(box2);
    }

    /**
     * Assumption : the box was filled starting from the first position
     * Return the ResourceType of the required box
     * @param position number of box I want the resource type
     * @return
     */
    public ResourceType getResourceType(int position){
        return boxes.get(position)[0].getType();
    }

    /**
     * Assumption : the controller has already verified the resourceType is not present in others box
     *      and the numberResources is not bigger than free space
     *
     * Add resources in the specified origin box starting from the first free position
     * @param originBox box I would like to insert the resources
     * @param numberResources number of resources I would like to insert
     * @param resourceType type of the resources I'm adding
     */
    public void addResources(int originBox , int numberResources , ResourceType resourceType){
        Resource[] box = boxes.get(originBox);
        int i = 0;
        int maxDim = box.length;//this can be eliminated if the controller do all the control

        while(numberResources != 0 && i < maxDim){
            if(box[i] == null){
                box[i] = new Resource(resourceType);
                numberResources--;
            }
            i++;
        }
        boxes.set(originBox , box);
    }

    /**
     * Return the quantity of resources in the box specified by originBox
     * @param originBox
     * @return
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
     * Assumption: the controller has already done the check about the available resources
     *
     * Eliminate resources from the last position to the first position
     * @param originBox
     * @param numberResources
     */
    public void useResources(int originBox , int numberResources){
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

    public void moveResources(int originBox , int destinationBox){

    }

    /**
     * add into boxes a new box of variable dimension in case of special ability
     *
     * @param howBig dimension of the new box
     */
    public void addBox(int howBig){
        Resource[] newBox = new Resource[howBig];
        boxes.add(newBox);
    }
}
