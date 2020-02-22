package com.company;

import com.company.interactives.Enemy;
import com.company.interactives.NPC;
import com.company.interactives.Player;
import com.company.interactives.Trap;

import java.util.Scanner;

public class Game {

    private static final String VERTICAL_WALL_CHAR = "|";
    private static final String HORIZONTAL_WALL_CHAR = "-";
    private static final String EMPTY_FIELD_CHAR = " ";
    private static final String PLAYER_CHAR = "P";
    private static final String ENEMY_CHAR = "M";
    private static final String EXIT_CHAR = "W";
    private static final String NPC_CHAR = "N";
    private static final String TRAP_CHAR = "T";

    private Player player;
    private Enemy enemy;
    private NPC npc;
    private Trap trap;

    private int width;
    private int height;

    private Field[][] map;

    public Game()
    {
        player = new Player();
        enemy = new Enemy();
        npc = new NPC();
        trap = new Trap();

        width = 5;
        height = 5;

        map = new Field[width][height];
        createMap();
    }

    private void createMap() {
        map[0][0] = new Field(false, true, false, false);
        map[1][0] = new Field(false, true, false, true);
        map[2][0] = new Field(false, true, true, false);
        map[3][0] = new Field(false, true, false, false, npc);
        map[4][0] = new Field(true, true, false, false);
        map[0][1] = new Field(true, true, false, true);
        map[1][1] = new Field(true, false, true, true, trap);
        map[2][1] = new Field(true, true, true, false);
        map[3][1] = new Field(true, false, false, true);
        map[4][1] = new Field(true, true, true, true, trap);
        map[0][2] = new Field(true, false, false, true);
        map[1][2] = new Field(true, false, true, false, npc);
        map[2][2] = new Field(true, true, false, true);
        map[3][2] = new Field(false, true, true, true, enemy);
        map[4][2] = new Field(true, false, true, false);
        map[0][3] = new Field(false, true, false, true);
        map[1][3] = new Field(true, true, true, false);
        map[2][3] = new Field(true, true, false, false);
        map[3][3] = new Field(true, false, false, true);
        map[4][3] = new Field(false, true, true, false);
        map[0][4] = new Field(true, false, false, true, player);
        map[1][4] = new Field(true, false, true, false);
        map[2][4] = new Field(true, false, false, true, enemy);
        map[3][4] = new Field(false, false, true, true);
        map[4][4] = new Field(true, false, true, false);
    }

    private void displayMap() {

    }

    public void showTradeLog() {
        System.out.println("You have opened the trade log. You can buy valuable items that will aid you in your journey");
        System.out.println("You can also sell items from your inventory. However, note that you cannot re-buy them.\n");

        System.out.println("Trade log open!\n");

        Scanner s = new Scanner(System.in);
        String input;

        //Looped until user exits
        do {
            System.out.println("Your inventory:\n");
            player.showInventory();

            System.out.println(npc.getName() + "'s inventory:");
            npc.showInventory();

            System.out.println("Do you want to buy or sell items?\n");
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("0. Leave");


            System.out.print("Please choose your option:");
            //Input is scanned
            input = s.nextLine();

            switch (input) {
                case "1":
                    buyItems();
                    break;
                case "2":
                    sellItems();
                    break;
                case "0":
                    System.out.println("You have opted to return to your adventure.");
                    return;
                default:
                    System.out.println("Your choice is not valid. Please try again!");
            }

        } while (!(input == "0"));

    }

    public void sellItems()
    {
        System.out.println("Please state what item you want to sell:");
        player.showInventory();

        Scanner s = new Scanner(System.in);
        String input;

            input = s.nextLine();
            switch(input)
            {

            }

    }

    public void buyItems()
    {

    }

}