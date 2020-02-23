package com.company.interactives;

import java.util.Random;

public class Enemy extends Character {

    public Enemy()
    {
        super();
    }

    /**
     * com.company.interactives.Enemy uses a basic attack on the player.
     * @return damage numbers
     */
    @Override
    public int attack()
    {
        Random rand = new Random();

        int damage = rand.nextInt(20);
        System.out.println("Monster has violently slammed you.");
        System.out.println("Monster has inflicted " + damage + " points of damage.");

        return damage;

    }

    /**
     * com.company.interactives.Enemy uses a special attack on the player.
     * It consumes mana.
     * @return damage numbers
     */
    @Override
    public int specialAttack()
    {
        Random rand = new Random();
        if (getMana() < 40)
        {
            System.out.println("Monster has ran out of focus to continue their attack for this turn.");
            return 0;
        }
        int damage = rand.nextInt(40);
        System.out.println("Monster reveals your darkest nightmares.");
        System.out.println("Monster has inflicted " + damage + " points of damage.");

        setMana(getMana() - 40);

        return damage;

    }

    /**
     * com.company.interactives.Enemy heals their health points.
     *
     */
    @Override
    public void healHP()
    {
        int HPPoints = 7;

        System.out.println("Monster absorbs the darkness of the void, recovering health.");
        System.out.println("Monster has gained " + HPPoints + " health points.");

        setHealth(getHealth() + HPPoints);
        if(getHealth() >100) setHealth(100);
    }

    /**
     * com.company.interactives.Enemy heals their mana points.
     *
     */
    @Override
    public void healMP()
    {
        int MPPoints = 10;

        System.out.println("Monster screams in anguish, replenishing its super powers.");
        System.out.println("Monster has gained " + MPPoints + " focus points.");

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
