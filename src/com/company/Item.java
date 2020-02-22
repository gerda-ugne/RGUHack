package com.company;

public class Item {

    private String itemName;
    private int price;

    public Item()
    {
        itemName = "default";
        price = 0;

    }

    public void showItemInformation()
    {
        System.out.println(itemName + ". Its price is " + price);
    }
    public String getItemName() {
        return itemName;
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

}
