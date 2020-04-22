package gerda.arcfej.dreamrealm;

import java.util.*;

/**
 * Inventory class contains a list
 * of inventory items as well as its capacity.
 *
 */
public class Inventory
{
    private List<Item> inventory;
    private int capacity;

    public Inventory() {
        capacity = 30;
        this.inventory = new ArrayList<>();

    }

    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Searches for an item in the inventory.
     * @param itemName item that is being searched
     * @return found item or null if not found
     */
    public Item returnItem(String itemName)
    {
        for (int i = 0;  i<inventory.size(); i++){
            String tempName = inventory.get(i).getItemName();
            if(tempName.equals(itemName)){

                return inventory.get(i);
            }
        }

        return null;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    /**
     * Adds item to the inventory by name
     * @param itemName - item to add
     */
    public void addToInventory(String itemName) {

        Item item = new Item(itemName, 30);

        if (inventory.size() < capacity) {
            inventory.add(item); }
        else System.out.println("Your inventory is full. You cannot pick up any more items!");
    }

    /**
     * Displays the inventory.
     */
    public void showInventory() {
        for (Item item : inventory) {
            item.showItemInformation();
        }

        if(inventory.size() == 0) System.out.println("Your inventory is empty! Pickup some items, trade or kill some monsters.");
    }

    /**
     * Removes an item from the inventory
     * @param itemName - removed item name
     * @return true if item is not on the inventory
     */
    public boolean remove(String itemName)
    {

        for (int i = 0;  i<inventory.size(); i++){
            String tempName = inventory.get(i).getItemName();
            if(tempName.equals(itemName)){
                inventory.remove(i);
                return true;
            }
        }

        return false;

    }

    /**
     *Resets the inventory
     */
    public void resetInventory()
    {
        this.inventory = new ArrayList<>();
    }

    /**
     * Finds an item in your inventory
     * @return true/false if the item was found
     */
    public boolean findInInventory(String itemName)
    {
        for (int i = 0;  i<inventory.size(); i++){
            String tempName = inventory.get(i).getItemName();
            if(tempName.equals(itemName)){

                return true;
            }
        }

        return false;
    }
}
