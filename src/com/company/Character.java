package com.company;

public abstract class Character {

    private int health;
    public int mana;
    private int currency;

    private Inventory inv = new Inventory();

    public Character(){

        health = 100;
        mana = 100;
        currency = 100;

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public void addToInventory(String item) {
        inv.addToInventory(item);
    }


    public void showInventory() {
        getInv().showInventory();
    }

    public boolean isCharacterAlive()
    {
        return getHealth() > 0;
    }

    public abstract int attack();

    public abstract int specialAttack();
    public abstract void healHP();

    public abstract void healMP();

    public abstract int execute(char move);
}
