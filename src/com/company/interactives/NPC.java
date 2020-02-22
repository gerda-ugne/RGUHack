package com.company.interactives;

public class NPC extends Character {

    private String name;

    public NPC()
    {
        super();
        name = "NPC";

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
    public void healMP() {

    }

    @Override
    public int execute(char move) {
        return 0;
    }
}
