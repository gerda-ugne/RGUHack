package com.company;

/**
 * Item class contains items that go into the inventory.
 */
public class Item {

    String itemName;
    private int price;

    public Item()
    {
        itemName = "default";
        price = 30;
    }
    public Item(String name, int price)
    {
        itemName = name;
        this.price = price;

    }

    /**
     * Displays the information about the item.
     */
    public void showItemInformation()
    {
        System.out.println( itemName+ ". Its price is " + price + ".");
    }


    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }
}
