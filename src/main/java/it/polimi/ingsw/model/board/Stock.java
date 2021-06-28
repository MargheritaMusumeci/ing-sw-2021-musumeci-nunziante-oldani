package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.osservables.StockObservable;

import java.io.Serializable;
import java.util.*;

/**
 * Other classes will see the stock as a single ArrayList of box,
 *      so originBox should be between 0 and (boxes.length + boxPlus.length)
 */
public class Stock extends StockObservable implements Serializable {
    private ArrayList<Resource[]> boxes;//standard boxes
    private ArrayList<Resource[]> boxPlus;//added boxes due to leader card ability
    private ArrayList<Resource> resourcesPlus;//this arrayList contains the allowed resources in boxPlus, at the same position

    /**
     * Initialize an empty stock
     */
    public Stock(){
        boxes = new ArrayList<>();
        Resource[] box0 = new Resource[1];
        Resource[] box1 = new Resource[2];
        Resource[] box2 = new Resource[3];
        Arrays.fill(box0 , null);
        Arrays.fill(box1 , null);
        Arrays.fill(box2 , null);
        boxes.add(box0);
        boxes.add(box1);
        boxes.add(box2);

        boxPlus = new ArrayList<>();
        resourcesPlus = new ArrayList<>();
    }

    /**
     * Private method that returns the box required: in boxes or in boxPlus
     * @param originBox is a number between 0 and boxes.size() + boxPlus.size()
     * @return the box required , null is the box doesn't exist
     */
    private Resource[] getBox(int originBox){
        if(originBox >= 0 && originBox < boxes.size()) return boxes.get(originBox).clone();
        if(originBox >= boxes.size() && originBox < boxes.size() + boxPlus.size()) return boxPlus.get(originBox - boxes.size()).clone();
        return null;
    }

    /**
     * Private method that sets a box in the originBox: in boxes or in boxPlus
     * @param originBox is the box required
     * @param box is the new box that will substitute the older in originBox position
     */
    private void setBox(int originBox , Resource[] box){
        if(originBox >= 0 && originBox < boxes.size())
            boxes.set(originBox , box);
        if(originBox >= boxes.size() && originBox < boxes.size() + boxPlus.size())
            boxPlus.set(originBox - boxes.size() , box);

        notifyStockListener(this);
    }

    /**
     * Assumption : the box was filled starting from the first position
     * Method that returns the resourceType accordingly to the box required: common box or added box
     * @param originBox number of box I want the resource type
     * @return the ResourceType of the required box
     */
    public Resource getResourceType(int originBox) {
        if(originBox < 0 || originBox >= getNumberOfBoxes())
            return null;
        if(originBox < boxes.size())
            return boxes.get(originBox)[0];
        else
            return resourcesPlus.get(originBox - boxes.size());
    }

    /**
     * @param originBox number of box we are interested
     * @return the quantity of resources in the box specified by originBox.
     *          If the box doesn't exist this method returns 0
     */
    private int getQuantities(int originBox){
        int quantity = 0;
        int i = 0;

        Resource[] box = getBox(originBox);

        if(box != null){
            int maxDim = box.length;
            while(i < maxDim){
                if(box[i] != null)
                    quantity++;
                i++;
            }
        }
        return quantity;
    }

    /**
     * Method that return the total number of a resource type in the stock
     * @param resourceType is the type I want the quantity
     * @return the number of resourceType
     */
    public int getTotalQuantitiesOf(Resource resourceType){
        int numOfBox = boxes.size();
        int numOfBoxPlus = 0;

        if(boxPlus != null) numOfBoxPlus = boxPlus.size();

        int quantities = 0;

        for(int i = 0; i < numOfBox; i++){
            if(getResourceType(i) == resourceType)
                quantities += getQuantities(i);
        }
        for(int i = 0; i < numOfBoxPlus; i++){
            Resource[] boxPlus = getBox(numOfBox + i);
            if(getResourceType(numOfBox + i) == resourceType)
                quantities += getQuantities(numOfBox + i);
        }
        return quantities;
    }

    /**
     * Method that returns the length of originBox. It returns 0 if the box doesn't exist.
     * @param originBox is the box I want the length
     * @return the length of the box in originBox position
     */
    public int getBoxLength(int originBox){
        Resource[] box = getBox(originBox);

        if(box == null)
            return 0;
        else
            return box.length;
    }

    /**
     * Eliminate resources from the last position to the first position
     * @param originBox number of box we are interested
     * @param numberResources number of resources we use
     * @throws NotEnoughResourcesException if the number of the resources required is smaller than the number of available resources
     */
    private void useResources(int originBox , int numberResources) throws NotEnoughResourcesException{
        if(numberResources > getQuantities(originBox)) throw new NotEnoughResourcesException("You don't have enough resources");

        Resource[] box = getBox(originBox);
        int i = box.length;

        while(numberResources != 0)
        {
            if(box[i - 1] != null){
                box[i - 1] = null;
                numberResources--;
            }
            i--;
        }
        setBox(originBox , box);

        notifyStockListener(this);
    }

    /**
     * Method that use an amount of resources automatically, starting from the box 0 to the last
     * @param numberResources is the number of resources to use
     * @param resourceType is the type of the resources to use
     * @throws NotEnoughResourcesException if there isn't enough resources in the stock
     */
    public void useResources(int numberResources , Resource resourceType) throws NotEnoughResourcesException{
        int numBox = getNumberOfBoxes();

        if(getTotalQuantitiesOf(resourceType) < numberResources)
            throw new NotEnoughResourcesException("Not enough resources in the Stock");

        for(int i = 0; i < numBox && numberResources > 0; i++){
            if(getResourceType(i) == resourceType){
                int quantity = getQuantities(i);
                if(quantity > numberResources)
                    quantity = numberResources;
                try {
                    useResources(i , quantity);
                    numberResources -= quantity;
                }catch(NotEnoughResourcesException e){
                    throw new NotEnoughResourcesException("Not enough resources in this box");
                }
            }
        }

        notifyStockListener(this);
    }

    /**
     * Add into boxes a new box of variable dimension in case of special ability
     * @param howBig dimension of the new box
     * @param resourceType is the only type of resources allowed in the box
     */
    public void addBox(int howBig , Resource resourceType){
        Resource[] newBox = new Resource[howBig];
        if (boxPlus==null) boxPlus = new ArrayList<>();
        boxPlus.add(newBox);
        resourcesPlus.add(resourceType);

        notifyStockListener(this);
    }

    /**
     * @return number of standard boxes in stock
     */
    public int getNumberOfBoxes(){
        return boxPlus!=null ? boxes.size() + boxPlus.size(): boxes.size();
    }

    /**
     * Method that return the whole amount of resources in the stock
     * This method will be use to check the winner in case of draw
     * @return the whole amount of resources in the stock
     */
    public int getTotalNumberOfResources(){
        int numOfBox = getNumberOfBoxes();
        int result = 0;
        for(int i = 0; i < numOfBox; i++){
            result += getQuantities(i);
        }
        return result;
    }

    /**
     * Manage stock for adding new resources. This method creates a temporary Stock.
     * Tries to insert all the resources starting with the more numerous.
     * If it manages to insert all the resources then it replaces the new stock with the old one.
     * @param resourceList new resources added to stock
     * @return true if all is completed successfully
     */
    public boolean manageStock(List<Resource> resourceList) {
        //collect resources
        HashMap<Resource, Integer> totalResources = new HashMap<>();

        //create false stock
        ArrayList<Resource[]> boxes2 = new ArrayList<>();
        Resource[] box0 = new Resource[1];
        Resource[] box1 = new Resource[2];
        Resource[] box2 = new Resource[3];
        Arrays.fill(box0, null);
        Arrays.fill(box1, null);
        Arrays.fill(box2, null);
        ArrayList<Resource[]> boxPlus2 = null;

        //just for prevent errors
        ArrayList<Resource> resourcesCopy = new ArrayList<>();
        resourcesCopy.addAll(resourceList);

        for(Resource resource: resourcesCopy){
            if(resource.equals(Resource.FAITH)) resourceList.remove(Resource.FAITH);
            if(resource.equals(Resource.WISH)) resourceList.remove(Resource.WISH);
            if(resource.equals(Resource.NOTHING)) resourceList.remove(Resource.NOTHING);
        }

        if (resourceList != null && resourceList.size() != 0) {
            for (Resource resource : resourceList) {
                totalResources.merge(resource, 1, Integer::sum);
            }

            for (int i = 0; i < getNumberOfBoxes(); i++) {
                if (getResourceType(i) != null) {
                    totalResources.merge(getResourceType(i), getQuantities(i), Integer::sum);
                }
            }
            if (totalResources.size() > getNumberOfBoxes()) {
                notifyStockListener(this);
                return false;
            }
            if (totalResources.size() == 0) {
                notifyStockListener(this);
                return true;
            }

            Resource resourceType;

            if (getNumberOfBoxes() > 3) { //if there is more stock space
                int space = getNumberOfBoxes() - 3;
                int i = 0; // for saving the index of boxPlus boxes
                boxPlus2 = new ArrayList<>();
                while (space > 0) {

                    int boxPlusDimension = this.boxPlus.get(i).length;
                    Resource[] resourcesPlusManage = new Resource[boxPlusDimension];
                    resourceType = this.resourcesPlus.get(i);
                    int boxIndex = 0;
                    while (totalResources.get(resourceType) >0 && boxPlusDimension > boxIndex) {
                        resourcesPlusManage[boxIndex] = resourceType;
                        boxIndex++;
                        totalResources.merge(resourceType, -1, Integer::sum);
                    }
                    boxPlus2.add(resourcesPlusManage);
                    if (totalResources.get(resourceType) < 1) totalResources.remove(resourceType);
                    space--;
                    i++;
                }
            }

            int fullBoxes = 0;
            while (totalResources.size() > 0) {
                int maxResources = 0;
                Set<Resource> allKeys = totalResources.keySet();
                ArrayList<Resource> allKeysList = new ArrayList<>(allKeys);
                resourceType = allKeysList.get(0);

                //find the resource with the max number of items in the map
                for (int i = 0; i < allKeys.size(); i++) {
                    if (maxResources < totalResources.get(allKeysList.get(i))) {
                        maxResources = totalResources.get(allKeysList.get(i));
                        resourceType = allKeysList.get(i);
                    }
                }

                // if number of resources is greater thant the possible space available return false
                if (totalResources.get(resourceType) > 3 - fullBoxes) {
                    notifyStockListener(this);
                    return false;
                }
                int i = 0;

                //fullBoxes represents which box has to be fulfilled in this case
                //boxes are fulfilled from the greatest to the smallest
                switch (fullBoxes) {
                    case (0):
                        while (totalResources.get(resourceType) > 0 && i < 3) {
                            box2[i] = resourceType;
                            i++;
                            totalResources.merge(resourceType, -1, Integer::sum);
                        }
                        totalResources.remove(resourceType);
                        break;
                    case (1):
                        while (totalResources.get(resourceType) > 0 && i < 2) {
                            box1[i] = resourceType;
                            i++;
                            totalResources.merge(resourceType, -1, Integer::sum);
                        }
                        totalResources.remove(resourceType);
                        break;
                    case (2):
                        while (totalResources.get(resourceType) > 0 && i < 1) {
                            box0[i] = resourceType;
                            i++;
                            totalResources.merge(resourceType, -1, Integer::sum);
                        }
                        totalResources.remove(resourceType);
                        break;
                }
                fullBoxes++;
            }
            boxes2.add(box0);
            boxes2.add(box1);
            boxes2.add(box2);

            this.boxes = boxes2;
            this.boxPlus = boxPlus2;


            notifyStockListener(this);

        }
        return true;
    }

    /**
     * @return an arrayList of standard boxes
     */
    public ArrayList<Resource[]> getBoxes () {
        return boxes;
    }

    /**
     * @return an arrayList of plus boxes
     */
    public ArrayList<Resource[]> getBoxPlus () {
        return boxPlus;
    }

    /**
     * @return an arrayList that contains which kind of resources can be stored in plus boxes
     */
    public ArrayList<Resource> getResourcesPlus () {
        return resourcesPlus;
    }

    /**
     * @return true if the stock is empty, false otherwise
     */
    public boolean stockIsEmpty () {
            int resources = 0;
            for (int i = 0; i < getNumberOfBoxes(); i++) {
                resources = resources + getQuantities(i);
            }
            return resources == 0;
        }
}
