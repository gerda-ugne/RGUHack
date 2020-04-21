package gerda.arcfej.dreamrealm.interactives;

import java.util.Random;

/**
 * Player class contains information about the player.
 * Combat methods and item consumption methods are also included.
 */
public class Player extends Character {

    public final int MAX_OIL;

    private int oil;

    /**
     * Constructor for Player class
     * @param maxOil
     */
    public Player(int maxOil)
    {
        super();
        MAX_OIL = maxOil;
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
        if(getPower() < 40)
        {
            System.out.println("You don't have enough will power to cast the special attack! You rest.");
            return 0;
        }

        int damage = rand.nextInt(50);
        System.out.println("You have inflicted " + damage + " points of damage.");
        setPower(getPower() - 40);

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
        itemFound = getInv().remove("Dreamcatcher");

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
    public void healPW()
    {
        boolean itemFound = false;
        itemFound = getInv().remove("Liquid light");


        if(itemFound == true)
        {
            int PWPoints = 50;

            System.out.println("You have used a liquid light potion to restore your will power.");
            System.out.println("You have gained " + PWPoints + " will power points.");

            setPower(getPower() + PWPoints);
            if(getPower() > 100) setPower(100);

        }
        else
        {
            System.out.println("You do not have any potions left! You rest.");
        }
    }

    public void addOil(int add)
    {
        oil = oil + add;
        if(oil>100) oil = 100;

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
                case '4': healPW(); break;

            }

        return damage;
    }

    /**
     * Steps out of combat.
     */
    public void flee()
    {
        System.out.println("You've chosen to flee your nightmares.");
    }


}
