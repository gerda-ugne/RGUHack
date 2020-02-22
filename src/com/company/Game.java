package com.company;

import com.company.interactives.*;

import java.util.Scanner;

public class Game {

    private static final String VERTICAL_WALL_CHAR = " | ";
    private static final String HORIZONTAL_WALL_CHAR = "---";
    private static final String EMPTY_FIELD_CHAR = "   ";
    private static final String PLAYER_CHAR = " P ";
    private static final String ENEMY_CHAR = " M ";
    private static final String EXIT_CHAR = " E ";
    private static final String NPC_CHAR = " N ";
    private static final String TRAP_CHAR = " T ";

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
        map[1][2] = new Field(true, true, true, false, npc);
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

    public void displayMap() {
        System.out.print(VERTICAL_WALL_CHAR);
        for (int i = 0; i < width; i++) {
            Field field = map[i][0];
            System.out.print((field.up ? EMPTY_FIELD_CHAR : HORIZONTAL_WALL_CHAR) + VERTICAL_WALL_CHAR);
        }
        System.out.println();
        for (int j = 0; j < height; j++) {
            System.out.print(map[0][j].left ? EMPTY_FIELD_CHAR : VERTICAL_WALL_CHAR);
            for (int i = 0; i < width; i++) {
                Field field = map[i][j];
                System.out.print(getInteractiveChar(field.getInteractive()) + (field.right ? EMPTY_FIELD_CHAR : VERTICAL_WALL_CHAR));
            }
            System.out.println();
            System.out.print(VERTICAL_WALL_CHAR);
            for (int i = 0; i < width; i++) {
                Field field = map[i][j];
                System.out.print((field.down ? EMPTY_FIELD_CHAR : HORIZONTAL_WALL_CHAR) + VERTICAL_WALL_CHAR);
            }
            System.out.println();
        }
    }

    private String getInteractiveChar(Interactive interactive) {
        if (interactive instanceof Player) {
            return PLAYER_CHAR;
        } else if (interactive instanceof Enemy) {
            return ENEMY_CHAR;
        } else if (interactive instanceof NPC) {
            return NPC_CHAR;
        } else if (interactive instanceof Trap) {
            return TRAP_CHAR;
        } else {
            return EMPTY_FIELD_CHAR;
        }
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
            System.out.println("You currently have " + player.getCurrency() + " coins.\n");

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

    public void sellItems() {
        System.out.println("Please state what item you want to sell:");
        player.showInventory();

        Scanner s = new Scanner(System.in);
        String input;

        input = s.nextLine();
        //If item is found in inventory
        if (player.getInv().findInInventory(input))
        {
            player.getInv().remove(input);
            player.setCurrency(player.getCurrency() + player.getInv().returnItem(input).getPrice());
        }
        else
        {
            //If item doesn't exist
            System.out.println("Item not found! Try again");
        }

    }

    public void buyItems()
    {

    }

}