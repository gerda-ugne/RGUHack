package com.company;

import java.util.*;

public class Inventory
{
    private List<Item> inventory;
    private int capacity;

    public Inventory() {
        capacity = 30;
        this.inventory = new ArrayList<>();

//        inventory.add(NUTRIENTS);
//        inventory.add(NUTRIENTS);
//        inventory.add(WAX);
//        inventory.add(WAX);
    }

    public List<Item> getInventory() {
        return inventory;
    }

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

    public void addToInventory(String itemName) {

        Item item = new Item(itemName, 30);

        if (inventory.size() < capacity) {
            inventory.add(item); }
        else System.out.println("Your inventory is full. You cannot pick up any more items!");
    }

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
