package com.company;

import com.company.Character;

import java.util.Random;

public class Enemy extends Character {

    public Enemy()
    {
        super();
    }

    /**
     * com.company.Enemy uses a basic attack on the player.
     * @return damage numbers
     */
    @Override
    public int attack()
    {
        Random rand = new Random();

        int damage = rand.nextInt(20);
        System.out.println("Human has thrown a plastic bag at you!");
        System.out.println("Human has inflicted " + damage + " points of damage.");

        return damage;

    }

    /**
     * com.company.Enemy uses a special attack on the player.
     * It consumes mana.
     * @return damage numbers
     */
    @Override
    public int specialAttack()
    {
        Random rand = new Random();
        if (getMana() < 40)
        {
            System.out.println("Human does not have enough energy to cast a special attack. They rest.");
            return 0;
        }
        int damage = rand.nextInt(40);
        System.out.println("Human has thrown a plastic bottle at you!");
        System.out.println("Human has inflicted " + damage + " points of damage.");

        setMana(getMana() - 40);

        return damage;

    }

    /**
     * com.company.Enemy heals their health points.
     *
     */
    @Override
    public void healHP()
    {
        int HPPoints = 7;

        System.out.println("Human has a snack to restore their health!");
        System.out.println("Human has gained " + HPPoints + " health points.");

        setHealth(getHealth() + HPPoints);
        if(getHealth() >100) setHealth(100);
    }

    /**
     * com.company.Enemy heals their mana points.
     *
     */
    @Override
    public void healMP()
    {
        int MPPoints = 10;

        System.out.println("Human has an energy drink!");
        System.out.println("Human has gained " + MPPoints + " mana points.");

        setMana(getMana() + MPPoints);
        if(getMana() >100) setMana(100);
    }


    /**
     * Executes choices that enemy AI has made by calling appropriate methods
     * @param move - randomly generated
     * @return damage - returns damage done to the player in total
     */
    @Override
    public int execute(char move)
    {

        //Variables to record the difference after the moves are done
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

}
