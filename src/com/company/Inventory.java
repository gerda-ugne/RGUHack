package com.company;

import java.util.*;

public class Inventory
{
    public static final String HP_POTION = "Nutrients - Use these when you're low on health";
    public static final String MP_POTION = "Wax - Wax restores your energy levels!";
    private List<String> inventory;
    private int capacity;

    public Inventory() {
        capacity = 30;
        this.inventory = new ArrayList<>();

//        inventory.add(NUTRIENTS);
//        inventory.add(NUTRIENTS);
//        inventory.add(WAX);
//        inventory.add(WAX);
    }

    public void addToInventory(String item) {
        if (inventory.size() < capacity) {
            inventory.add(item);
        } else System.out.println("Your inventory is full. You cannot pick up any more items!");
    }

    public void showInventory() {
        for (String item : inventory) {
            System.out.println(item);
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
            String tempName = inventory.get(i);
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
}
