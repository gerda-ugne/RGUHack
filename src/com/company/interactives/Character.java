package com.company.interactives;

import com.company.Inventory;

public abstract class Character extends Interactive {

    protected boolean visible;
    private int health;
    public int mana;
    private int currency;

    private Inventory inv = new Inventory();

    public Character(){

        health = 100;
        mana = 100;
        currency = 100;

    }

    @Override
    public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }

    @Override
    public boolean isVisible() {
        return visible;
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

    public boolean isCharacterAlive()
    {
        return getHealth() > 0;
    }


    public void checkStatus(){

        System.out.println("Health:  " + health);
        System.out.println("Mana:  " + mana);

    }

    public abstract int attack();

    public abstract int specialAttack();
    public abstract void healHP();

    public abstract void healMP();

    public abstract int execute(char move);
}
