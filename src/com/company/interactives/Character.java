package com.company.interactives;

import com.company.Inventory;

/**
 * Abstract character class that contains
 * main fields needed for implementing the player, enemy
 * and npc characters in the game.
 *
 */
public abstract class Character extends Interactive {

    public static final int MAX_HEALTH = 100;
    public static final int MAX_POWER = 100;
    public static final int MAX_CURRENCY = 100;

    protected boolean visible;
    private int health;
    public int power;
    private int currency;

    private Inventory inv = new Inventory();

    public Character(){

        health = MAX_HEALTH;
        power = MAX_POWER;
        currency = MAX_CURRENCY;

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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
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
        System.out.println("Will power:  " + power);

    }

    public abstract int attack();

    public abstract int specialAttack();
    public abstract void healHP();

    public abstract void healPW();

    public abstract int execute(char move);
}
