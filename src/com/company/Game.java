package com.company;

import com.company.interactives.*;
import com.company.map.Field;

import java.util.Scanner;

public class Game {

    private static final String VERTICAL_WALL = "|";
    private static final String HORIZONTAL_WALL = "---";
    private static final String EMPTY_FIELD = "   ";
    private static final String NO_WALL = " ";
    private static final String PLAYER = " P ";
    private static final String ENEMY = " M ";
    private static final String EXIT = " E ";
    private static final String NPC = " N ";
    private static final String TRAP = " T ";

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
        player.setPosition(2, 2);
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
        for (int j = player.getY() - 2; j <= player.getY() + 2; j++) {
            displayRowUpperBorders(j);
            displayDataRow(j);
        }
        displayRowBottomBorders(player.getY() + 2);
    }

    private void displayDataRow(int row) {
        for (int i = player.getX() - 2; i <= player.getX() + 2; i++) {
            Field field;
            try {
                field = map[i][row];
                System.out.print((field.left ? NO_WALL : VERTICAL_WALL) + getInteractiveChar(field.getInteractive()));
            } catch (IndexOutOfBoundsException e) {
                System.out.print(NO_WALL + EMPTY_FIELD);
            }
        }
        if (row >= 0 && row < height) {
            System.out.println(map[width - 1][row].right ? NO_WALL : VERTICAL_WALL);
        } else {
            System.out.println(NO_WALL);
        }
    }

    private void displayRowUpperBorders(int row) {
        for (int i = player.getX() - 2; i <= player.getX() + 2; i++) {
            try {
                Field field = map[i][row];
                System.out.print(VERTICAL_WALL + (field.up ? EMPTY_FIELD : HORIZONTAL_WALL));
            } catch (IndexOutOfBoundsException e) {
                System.out.print(NO_WALL + EMPTY_FIELD);
            }
        }
        if (row >= 0 && row < height) {
            System.out.println(VERTICAL_WALL);
        } else {
            System.out.println(NO_WALL);
        }
    }

    private void displayRowBottomBorders(int row) {
        for (int i = player.getX() - 2; i <= player.getX() + 2; i++) {
            try {
                Field field = map[i][row];
                System.out.print(VERTICAL_WALL + (field.down ? EMPTY_FIELD : HORIZONTAL_WALL));
            } catch (IndexOutOfBoundsException e) {
                System.out.print(NO_WALL + EMPTY_FIELD);
            }
        }
        if (row >= 0 && row < height) {
            System.out.println(VERTICAL_WALL);
        } else {
            System.out.println(NO_WALL);
        }
    }

    private String getInteractiveChar(Interactive interactive) {
        if (interactive instanceof Player) {
            return PLAYER;
        } else if (interactive instanceof Enemy) {
            return ENEMY;
        } else if (interactive instanceof NPC) {
            return NPC;
        } else if (interactive instanceof Trap) {
            return TRAP;
        } else {
            return EMPTY_FIELD;
        }
    }

    /**
     * Displays the trade log when the player encounters a NPC and chooses the trade option.
     */
    public void showTradeLog() {
        System.out.println("You have opened the trade log. You can buy valuable items that will aid you in your journey");
        System.out.println("You can also sell items from your inventory. However, note that you cannot re-buy them.\n");

        System.out.println("Trade log open!\n");

        Scanner s = new Scanner(System.in);
        String input;

        //Looped until user exits
        do {
            System.out.println("Your inventory:\n");
            player.getInv().showInventory();
            System.out.println("You currently have " + player.getCurrency() + " coins.\n");

            System.out.println(npc.getName() + "'s inventory:");
            npc.getInv().showInventory();

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

    /**
     * Method for selling items. This option is selected from the trade log.
     */
    public void sellItems() {

        do {

            System.out.println("\nPlease state what item you want to sell, or enter 'return' to return:");
            player.getInv().showInventory();

            Scanner s = new Scanner(System.in);
            String input;

            input = s.nextLine();
            if(input == "return") return;
            //If item is found in inventory
            if (player.getInv().findInInventory(input))
            {
                player.setCurrency(player.getCurrency() + player.getInv().returnItem(input).getPrice());
                player.getInv().remove(input);
                npc.getInv().addToInventory(input);
                System.out.println("\nYou currently have: " + player.getCurrency() + " coins.");
            }
            else
            {
                //If item doesn't exist
                System.out.println("Item not found! Try again");
            }

            // Additional menu, restarts if input is wrong
            do {

                System.out.println("\nDo you want to sell more items?\n");
                System.out.println("1. Yes, I have goods to provide");
                System.out.println("2. No, take me back to the trade log");

                input = s.nextLine();
                switch (input)
                {
                    case "1": break;
                    case "2": return;
                    default: System.out.println("Invalid option! Please choose an integer 1 or 2.");
                }

            } while (!(input.equals("1")));

        } while (true);


    }

    /**
     * Method for buying items. This option is accessible from the trade log.
     */
    public void buyItems()
    {
        do {
            System.out.println("\nPlease state what item you want to buy, or enter 'return' to return:");

            Scanner s = new Scanner(System.in);
            String input;

            input = s.nextLine();
            if(input == "return") return;
            //If item is found in NPC inventory
            if (npc.getInv().findInInventory(input))
            {
                //Checks if player can afford the item
                if(player.getCurrency() >= npc.getInv().returnItem(input).getPrice())
                {
                    player.setCurrency(player.getCurrency() - npc.getInv().returnItem(input).getPrice());
                    npc.getInv().remove(input);
                    player.getInv().addToInventory(input);
                    System.out.println("\nYou currently have: " + player.getCurrency() + " coins.");
                }
                else
                {
                    System.out.println("You do not have enough coins to buy this item.");
                }

            }
            else
            {
                //If item doesn't exist
                System.out.println("Item not found! Try again");
            }

            // Additional menu, restarts if input is wrong
            do {

                System.out.println("\nDo you want to buy more items?\n");
                System.out.println("1. Yes, I'm interested in more goods");
                System.out.println("2. No, take me back to the trade log");

                input = s.nextLine();
                switch (input)
                {
                    case "1": break;
                    case "2": return;
                    default: System.out.println("Invalid option! Please choose an integer 1 or 2.");
                }

            } while (!(input.equals("1")));
        } while (true);
    }

    /**
     * Moves player in a selected location
     * @param direction - input taken from the user
     * @return true/false depending on whether the player can move
     */
    public boolean movePlayer(char direction)
    {
        int playerX = player.getX();
        int playerY = player.getY();

        boolean canMove = false;
        int sizeOfField = 2;

        try {
            //up
            if(direction == 'u')
            {
                canMove = map[playerX][playerY].up;
                if(canMove)
                {
                    player.setY(playerY+sizeOfField);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            //left
            else if(direction == 'l')
            {
                canMove = map[playerX][playerY].left;
                if(canMove)
                {
                    player.setX(playerX-sizeOfField);
                    return true;
                }
                else
                {
                    return false;
                }

            }
            //right
            else if(direction == 'r')
            {
                canMove = map[playerX][playerY].right;
                if(canMove)
                {
                    player.setX(playerX + sizeOfField);
                    return true;
                }
                else
                {
                    return false;
                }

            }
            //down
            else if(direction == 'd')
            {
                canMove = map[playerX][playerY].down;
                if(canMove)
                {
                    player.setY(playerY - sizeOfField);
                    return true;
                }
                else
                {
                    return false;
                }

            }
            else
            {
                //Invalid input therefore cannot move
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {

            return false;
        }
    }

}