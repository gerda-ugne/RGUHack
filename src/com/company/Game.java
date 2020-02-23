package com.company;

import com.company.interactives.*;
import com.company.map.Field;

import java.util.*;
import java.util.function.Predicate;

public class Game {

    private static final String VERTICAL_WALL = "|";
    private static final String HORIZONTAL_WALL = "---";
    private static final String EMPTY_FIELD = "   ";
    private static final String NO_WALL = " ";
    private static final String PLAYER = " P ";
    private static final String ENEMY = " M ";
    private static final String DEAD_ENEMY = " X ";
    private static final String EXIT = " E ";
    private static final String NPC = " N ";
    private static final String TRAP = " T ";

    private static final int ENEMIES_PERCENT = 12;
    private static final int NPCS_PERCENT = 6;
    private static final int TRAPS_PERCENT = 8;

    private Random rnd;

    private int width;
    private int height;

    private int viewDistance;

    private Field[][] map;

    private Player player;
    private Enemy enemy;
    private NPC npc;
    private Trap trap;

    private Interactive tempInteractive;

    public Game()
    {
        rnd = new Random();

        width = 20;
        height = 10;
        viewDistance = 20;

        player = new Player();
        tempInteractive = null;

        player.setPosition(rnd.nextInt(width), rnd.nextInt(height));
//        player.setPosition(0, 0);
//        enemy.setPosition(0, 1);
//        trap.setPosition(1, 0);

        map = new Field[width][height];
        createMap();

        map[player.getX()][player.getY()].setInteractive(player);
        generateMapItems();
    }

    private void createMap() {
        Set<Field> in = new HashSet<>(width * height);
        List<Field> neighbors = new ArrayList<>(width * height / 2);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Field field = new Field();
                map[i][j] = field;
                field.setPosition(i, j);
            }
        }

        int x = player.getX();
        int y = player.getY();
        Field newIn = map[x][y];
        in.add(newIn);
        do {
            List<Field> nexts = new ArrayList<>(4);
            try {
                nexts.add(map[x - 1][y]);
            } catch (Exception ignore) { }
            try {
                nexts.add(map[x + 1][y]);
            } catch (Exception ignore) { }
            try {
                nexts.add(map[x][y - 1]);
            } catch (Exception ignore) { }
            try {
                nexts.add(map[x][y + 1]);
            } catch (Exception ignore) { }
            for (Field next : nexts) {
                if (!in.contains(next)) {
                    neighbors.add(next);
                }
            }
            newIn = neighbors.remove(rnd.nextInt(neighbors.size()));
            x = newIn.getX();
            y = newIn.getY();

            Field finalNewIn = newIn;
            // Is field left against newIn?
            Predicate<Field> left = field -> field.getY() == finalNewIn.getY() && field.getX() + 1 == finalNewIn.getX();
            // is field right against newIn
            Predicate<Field> right = field -> field.getY() == finalNewIn.getY() && field.getX() - 1 == finalNewIn.getX();
            Predicate<Field> up = field -> field.getX() == finalNewIn.getX() && field.getY() + 1 == finalNewIn.getY();
            Predicate<Field> down = field -> field.getX() == finalNewIn.getX() && field.getY() - 1 == finalNewIn.getY();
            Field connect = in.stream()
                    .filter(left.or(right).or(up).or(down))
                    .findFirst()
                    .orElse(null);
            assert connect != null;
            // is connect up against newIn
            if (up.test(connect)) {
                connect.setDown(true);
                newIn.setUp(true);
            } else if (down.test(connect)) {
                connect.setUp(true);
                newIn.setDown(true);
            } else if (right.test(connect)) {
                connect.setLeft(true);
                newIn.setRight(true);
            } else if (left.test(connect)) {
                connect.setRight(true);
                newIn.setLeft(true);
            }
            in.add(newIn);
        } while (!neighbors.isEmpty());
    }

    int enemyNum;
    int npcNum;
    int trapNum;

    private void generateMapItems() {
        float max = width * height;
        enemyNum = Math.round(max * ENEMIES_PERCENT / 100);
        npcNum = Math.round(max * NPCS_PERCENT / 100);
        trapNum = Math.round(max * TRAPS_PERCENT / 100);

        for (int i = 0; i < width; i += 2) {
            for (int j = 0; j < height; j += 2) {
                placeInteractive(
                        i == 0 ? 0 : i - 1,
                        i >= width - 1 ? width - 2 : i + 2,
                        j == 0 ? 0 : j - 1,
                        j >= height - 1 ? height - 2 : j + 2
                );
            }
        }
        while (enemyNum + npcNum + trapNum > 0) {
            placeInteractive(0, width, 0, height);
        }
    }

    private void placeInteractive(int xMin, int xMax, int yMin, int yMax) {
        Interactive interactive = null;
        int x = 0;
        int y = 0;
        boolean allowed = false;

        while (!allowed) {
            do {
                x = rnd.nextInt(xMax - xMin) + xMin;
                y = rnd.nextInt(yMax - yMin) + yMin;
            } while (map[x][y].getInteractive() != null);
            switch (rnd.nextInt(3)) {
                case 0:
                    interactive = enemyNum > 0 ? new Enemy() : null;
                    break;
                case 1:
                    interactive = npcNum > 0 ? new NPC() : null;
                    break;
                case 2:
                    interactive = trapNum > 0 ? new Trap() : null;
            }
            allowed = isPlacementAllowed(interactive, x, y);
        }
        interactive.setPosition(x, y);
        map[x][y].setInteractive(interactive);
        if (interactive instanceof Enemy) enemyNum--;
        else if (interactive instanceof NPC) npcNum--;
        else if (interactive instanceof Trap) trapNum--;
        displayMap();
    }

    private boolean isPlacementAllowed(Interactive interactive, int x, int y) {
        if (interactive == null) {
            return false;
        }
        for (int i = (x == 0 ? x : x - 1); i <= (x >= width - 1 ? x : x + 1); i++) {
            for (int j = (y == 0 ? y : y - 1); j <= (j >= height - 1 ? y : j + 1); j++) {
                Interactive existing = map[i][j].getInteractive();
                if (existing != null && interactive.getClass().equals(existing.getClass())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void displayMap() {
        for (int j = player.getY() - viewDistance; j < player.getY() + viewDistance; j++) {
            displayRowUpperBorders(j);
            displayDataRow(j);
            if (j == height - 1) {
                displayRowBottomBorders(j);
            }
        }
    }

    private void displayDataRow(int row) {
        for (int i = player.getX() - viewDistance; i <= player.getX() + viewDistance; i++) {
            Field field;
            try {
                field = map[i][row];
                System.out.print((field.canLeft() ? NO_WALL : VERTICAL_WALL) + getInteractiveChar(field.getInteractive()));
                if (i == width - 1) {
                    System.out.print((field.canRight() ? NO_WALL : VERTICAL_WALL));
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.print(NO_WALL + EMPTY_FIELD);
            }
        }
        System.out.println();
    }

    private void displayRowUpperBorders(int row) {
        for (int i = player.getX() - viewDistance; i <= player.getX() + viewDistance; i++) {
            try {
                Field field = map[i][row];
                System.out.print(VERTICAL_WALL + (field.canUp() ? EMPTY_FIELD : HORIZONTAL_WALL));
                if (i == width - 1) {
                    System.out.print(VERTICAL_WALL);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.print(NO_WALL + EMPTY_FIELD);
            }
        }
        System.out.println();
    }

    private void displayRowBottomBorders(int row) {
        for (int i = player.getX() - viewDistance; i <= player.getX() + viewDistance; i++) {
            try {
                Field field = map[i][row];
                System.out.print(VERTICAL_WALL + (field.canDown() ? EMPTY_FIELD : HORIZONTAL_WALL));
                if (i == width - 1) {
                    System.out.print(VERTICAL_WALL);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.print(NO_WALL + EMPTY_FIELD);
            }
        }
        System.out.println();
    }

    private String getInteractiveChar(Interactive interactive) {
        if (interactive instanceof Player) {
            return PLAYER;
        } else if (interactive instanceof Enemy) {
            if (((Enemy) interactive).isCharacterAlive()) {
                return ENEMY;
            } else {
                return DEAD_ENEMY;
            }
        } else if (interactive instanceof NPC) {
            return NPC;
        } else if (interactive instanceof Trap) {
            return TRAP;
        } else {
            return EMPTY_FIELD;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public NPC getNpc() {
        return npc;
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

        } while (!(input.equals("0")));

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
            if(input.equals("return")) return;
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
            if(input.equals("return")) return;
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
    public boolean movePlayer(String direction)
    {
        int playerX = player.getX();
        int playerY = player.getY();

        int sizeOfField = 1;

        int newX;
        int newY;

        try {
            //up
            switch (direction) {
                case "u":
                    if (map[playerX][playerY].canUp()) {
                        newX = playerX;
                        newY = playerY - sizeOfField;
                    } else {
                        return false;
                    }
                    break;
                    //left
                case "l":
                    if (map[playerX][playerY].canLeft()) {
                        newX = playerX - sizeOfField;
                        newY = playerY;
                    } else {
                        return false;
                    }
                    break;
                    //right
                case "r":
                    if (map[playerX][playerY].canRight()) {
                        newX = playerX + sizeOfField;
                        newY = playerY;
                    } else {
                        return false;
                    }
                    break;
                    //down
                case "d":
                    if (map[playerX][playerY].canDown()) {
                        newX = playerX;
                        newY = playerY + sizeOfField;
                    } else {
                        return false;
                    }
                    break;
                default:
                    //Invalid input therefore cannot move
                    return false;
            }
            //Puts down the old interactive
            if (tempInteractive != null && !(tempInteractive.equals(player))) {
                map[tempInteractive.getX()][tempInteractive.getY()].setInteractive(tempInteractive);
                tempInteractive = null;
            } else {
                //Sets old position to have no interaction
                map[playerX][playerY].setInteractive(null);
            }

            //Saves old interactive
            tempInteractive = map[newX][newY].getInteractive();

            //Sets new position of the player
            player.setPosition(newX, newY);

            //Sets new player position to have the marker of the player
            map[newX][newY].setInteractive(player);

            return true;
        } catch (ArrayIndexOutOfBoundsException e) {

            //If out of boundaries cannot move
            return false;
        }
    }

}