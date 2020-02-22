package com.company;

import com.company.Character;
import com.company.Inventory;

import java.util.Random;

public class Player extends Character {

    private int oil;

    public Player()
    {
        super();
        oil = 100;
    }


    /**
     * com.company.Player uses a basic attack on the enemy.
     * @return damage numbers
     */
    public int attack()
    {
        Random rand = new Random();
        int damage = rand.nextInt(20);
        System.out.println("You pinch the human with your beak!");
        System.out.println("You have inflicted " + damage + " points of damage.");

        return damage;

    }
    /**
     * com.company.Player uses a special attack on the enemy.
     * It consumes MP.
     * @return damage numbers
     */
    @Override
    public int specialAttack()
    {
        Random rand = new Random();
        if(getMana() < 40)
        {
            System.out.println("You don't have enough energy to cast the special attack! You rest.");
            return 0;
        }

        int damage = rand.nextInt(50);
        System.out.println("You furiously flap your wings at the human, inflicting serious damage!");
        System.out.println("You have inflicted " + damage + " points of damage.");
        setMana(getMana() - 40);

        return damage;

    }

    /**
     * com.company.Player uses a health potion if they have one.
     *
     */
    @Override
    public void healHP()
    {
        boolean itemFound = false;
        itemFound = getInv().remove(Inventory.HP_POTION);

        if(itemFound == true)
        {
            int HPPoints = 40;

            System.out.println("You had a worm with a crunchy rock. You feel restored!");
            System.out.println("You have gained " + HPPoints + " health points.");

            setHealth(getHealth() + HPPoints);
            if(getHealth() > 100) setHealth(100);
        }
        else
        {
            System.out.println("You do not have any snacks left! You rest.");
        }

    }

    /**
     * com.company.Player uses a MP potion if they have one.
     *
     */
    @Override
    public void healMP()
    {
        boolean itemFound = false;
        itemFound = getInv().remove(Inventory.MP_POTION);


        if(itemFound == true)
        {
            int MPPoints = 50;

            System.out.println("You have used your wax for a power up!");
            System.out.println("You have gained " + MPPoints + " mana points.");

            setMana(getMana() + MPPoints);
            if(getMana() > 100) setMana(100);

        }
        else
        {
            System.out.println("You do not have any wax left! You rest.");
        }
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
        System.out.println("You've chosen to flee! You take off and fly away with your stash of plastic bags.");
    }



}
