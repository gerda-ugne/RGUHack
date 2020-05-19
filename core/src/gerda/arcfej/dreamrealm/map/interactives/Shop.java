package gerda.arcfej.dreamrealm.map.interactives;

/**
 * NPC class contains information about NPCs.
 *
 */
public class Shop extends Character {

    private String name;

    public Shop()
    {
        super();
        name = "Stranger";
        getInv().addToInventory("Oil");
        getInv().addToInventory("Oil");
        getInv().addToInventory("Liquid light potion");
        getInv().addToInventory("Liquid light potion");
        getInv().addToInventory("Dreamcatcher");
        getInv().addToInventory("Dreamcatcher");

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    @Override
    public int attack() {
        return 0;
    }

    @Override
    public int specialAttack() {
        return 0;
    }

    @Override
    public void healHP() {

    }

    @Override
    public void healPW() {

    }

    @Override
    public int execute(char move) {
        return 0;
    }
}
