package com.company.interactives;

import com.company.Inventory;

import java.util.Random;

public class Player extends Character {

    private int oil;

    public Player()
    {
        super();
        oil = 100;
        getInv().addToInventory("Rock");
    }


    /**
     * com.company.interactives.Player uses a basic attack on the enemy.
     * @return damage numbers
     */
    public int attack()
    {
        Random rand = new Random();
        int damage = rand.nextInt(20);
        System.out.println("You have inflicted " + damage + " points of damage.");

        return damage;

    }
    /**
     * com.company.interactives.Player uses a special attack on the enemy.
     * It consumes MP.
     * @return damage numbers
     */
    @Override
    public int specialAttack()
    {
        Random rand = new Random();
        if(getMana() < 40)
        {
            System.out.println("You don't have enough will power to cast the special attack! You rest.");
            return 0;
        }

        int damage = rand.nextInt(50);
        System.out.println("You have inflicted " + damage + " points of damage.");
        setMana(getMana() - 40);

        return damage;

    }

    /**
     * com.company.interactives.Player uses a health potion if they have one.
     *
     */
    @Override
    public void healHP()
    {
        boolean itemFound = false;
        itemFound = getInv().remove("HP_POTION");

        if(itemFound == true)
        {
            int HPPoints = 40;

            System.out.println("You use a dreamcatcher. You feel restored!");
            System.out.println("You have gained " + HPPoints + " health points.");

            setHealth(getHealth() + HPPoints);
            if(getHealth() > 100) setHealth(100);
        }
        else
        {
            System.out.println("You do not have any dreamcatchers left! You pass your turn.");
        }

    }

    /**
     * com.company.interactives.Player uses a MP potion if they have one.
     *
     */
    @Override
    public void healMP()
    {
        boolean itemFound = false;
        itemFound = getInv().remove("MP_Potion");


        if(itemFound == true)
        {
            int MPPoints = 50;

            System.out.println("You have used a liquid light potion to restore your will power.");
            System.out.println("You have gained " + MPPoints + " will power points.");

            setMana(getMana() + MPPoints);
            if(getMana() > 100) setMana(100);

        }
        else
        {
            System.out.println("You do not have any potions left! You rest.");
        }
    }

    public void addOil(int add)
    {
        oil = oil + add;
        if(oil>100) oil = 0;

    }

    public int getOil()
    {
        return oil;
    }
    public void setOil(int oil)
    {
        this.oil = oil;
    }

    /**
     * Executes choices that player has made by calling appropriate methods
     * @param move - move the player chose
     * @return damage - returns damage done to the enemy in total
     */
    @Override
    public int execute(char move)
    {
        int damage = 0;

            switch(move)
            {
                case '1': damage = damage + attack(); break;
                case '2': damage = damage + specialAttack(); break;
                case '3': healHP(); break;
                case '4': healMP(); break;

            }

        return damage;
    }

    public void flee()
    {
        System.out.println("You've chosen to flee your nightmares.");
    }


}
