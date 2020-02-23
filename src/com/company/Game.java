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


    boolean firstTrap = true;
    boolean firstMonster = true;
    boolean firstNPC = true;

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
    public boolean movePlayer(String direction) {
        int playerX = player.getX();
        int playerY = player.getY();

        int sizeOfField = 1;

        // return -1: death, 0: flee, 1: victory
        int outcome;

        int newX=0;
        int newY=0;

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

            if (tempInteractive instanceof Enemy) {
                combat();
            }
            if(tempInteractive instanceof Trap)
            {
                if(firstTrap = true) encounterTrap1();

                else gameOver();
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {

            //If out of boundaries cannot move
            return false;
        }
    }

    /**
     * Plays when you step on a trap
     */
    public void encounterTrap1()
    {
        if(firstTrap)
        {
            Scanner sc = new Scanner(System.in);
            String trapInput;

            System.out.println("\nClick, click. What is this sound? It comes from the eastern side. The noise is quite funny. ");
            System.out.println("Almost... Mechanical? You wonder why there would be anything machine-like. You turn to ");
            System.out.println("examine the source of the sound. There is an enormous rusty machine standing in your way. ");
            System.out.println("It does not look safe nor useful. It seems impossible to walk next to it, although, if you tried ");
            System.out.println("you might get crushed by all the moving parts. Maybe you could try and destroy it, like you do ");
            System.out.println("with everything in your life? You can spot some gears shining in the middle of it. Could a simple ");
            System.out.println("rock and strength of your amazing muscles solve the problem?");

            boolean retry = false;
            do {

                System.out.println("\n1.\tThrow a rock at it. What could go wrong?\n" +
                        "2.\tSqueeze next to it. How hard could it be?\n");

                trapInput = sc.nextLine();
                switch (trapInput)
                {
                    case"1": retry = false; continue;
                    case "2":
                    {
                        System.out.println("As you tried to squeeze next to the dangerously looking machine, " +
                                "your clothes got into the motor and your body got shattered. Good job. ");

                        gameOver();
                    }
                    default: System.out.println("Please check your input!"); retry=true;break;
                }
            } while (retry);

            firstTrap = false;
            System.out.println("\nThe rock hit the gears and the rusty parts fell apart, unblocking the path.");
            System.out.println("\nNEXT TIME YOU STEP ON A TRAP, YOU WILL BE FATALLY INJURED. BE AWARE!");
            System.out.println("USE THE CHECK FOR TRAP OPTION TO DISABLE THEM.\n");


            disableTrap();
            tempInteractive = null;

        }

    }


    /*
     * Disables traps if they exist
     * @return false/true whether there were traps to disable
     */
    public boolean disableTrap()
    {
        int playerX = player.getX();
        int playerY = player.getY();

        boolean trapExists = false;

        try {
            if(map[playerX+1][playerY].canRight())
            {
                if(map[playerX+1][playerY].getInteractive().equals(trap))
                {
                    trapExists=true;
                    map[playerX+1][playerY].setInteractive(null);
                    tempInteractive = null;

                }
            }
            if(map[playerX-1][playerY].canLeft() )
            {
                if(map[playerX-1][playerY].getInteractive().equals(trap))
                {
                    trapExists=true;
                    map[playerX-1][playerY].setInteractive(null);
                    tempInteractive = null;

                }
            }
            if(map[playerX][playerY-1].canUp() )
            {
                if(map[playerX][playerY-1].getInteractive().equals(trap))
                {
                    trapExists=true;
                    map[playerX][playerY-1].setInteractive(null);
                    tempInteractive = null;

                }
            }
            if(map[playerX][playerY-1].canDown())
            {
                if(map[playerX][playerY-1].getInteractive().equals(trap))
                {
                    trapExists=true;
                    map[playerX][playerY-1].setInteractive(null);
                    tempInteractive = null;

                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {



        }
        catch (NullPointerException e)
        {

        }


        return trapExists;


    }

    public void showCombatOptions()
    {
        System.out.println("\nRegular attacks\n");
        System.out.println("1. Try to cut through the monster’s chest. Maybe it does have a heart on the contrary to you?");
        System.out.println("2. Attempt to cut off the monster’s legs. Legless opponent sounds like something you could fight.");
        System.out.println("3. Throw the sword at the monster's head hoping that you aim properly and won’t lose your weapon.");
        System.out.println("4. Pirouette/Spin while blindly flinging the blade around like a sane person you are.");
        System.out.println("5. Just try to harm the monster with the sword somehow - you are so scared that you can barely move.\n");
        System.out.println("Special attacks that require your will power:\n");
        System.out.println("6. Try to tickle it to death with hands you create with the power of your mind. You brute. ");
        System.out.println("7. Create a fireball and throw it at the monster, might make it evaporate? Or create a sculpture out of it. That might be your chance to become a creepy artist. ");
        System.out.println("8. Try and dismember the monster’s body parts with the power of your mind to show off how almighty you are at the moment and start to wonder who the real monster is right now.");
        System.out.println("\nRestorative options:");
        System.out.println("9. Use a dreamcatcher to grasp your breath - Healing ability");
        System.out.println("10. Consume a liquid light potion to restore your will power - Restoration ability");
        System.out.println("0. Run away, like the coward you are");

    }

    // return -1: death, 0: flee, 1: victory
    public int combat() {
        System.out.println("\nYou feel like you're being watched. You might not be ready, but you must face your night terrors.");
        Scanner s = new Scanner(System.in);
        enemy = new Enemy();
        String userInput;
        String enemyInput;

        //Records damage enemy and player do each turn
        int enemyDamage = 0;
        int playerDamage = 0;

        //Retry if input is invalid
        boolean retry;

        do {

            System.out.println("\nYour status:");
            System.out.println("Your health: " + player.getHealth());
            System.out.println("Your will power: " + player.getMana());
            System.out.println();

            enemyDamage = 0;
            playerDamage = 0;

            do {
                System.out.println("Please choose your next move:");
                showCombatOptions();

                userInput = s.nextLine();
                retry = false;

                playerDamage = 0;
                switch (userInput) {
                    case "1": case "2": case "3": case"4": case"5":
                        playerDamage = playerDamage + player.attack();
                        break;
                    case "6": case"7": case"8":
                        playerDamage = playerDamage + player.specialAttack();
                        break;
                    case "9":
                        player.healHP();
                        break;
                    case "10":
                        player.healMP();
                        break;
                    case "0":
                        player.flee();
                        return 0;
                    default:
                        System.out.println("Please try again - wrong user input.");
                        retry = true;
                        break;
                }

            } while (retry);


            //Enemy health is deduced after player moves
            enemy.setHealth(enemy.getHealth() - playerDamage);
            if (!enemy.isCharacterAlive()) break;

            //Enemy has their turn
            System.out.println("\nMonster status:");
            System.out.println("Monster health: " + enemy.getHealth());
            System.out.println("Monster will power: " + enemy.getMana());
            ;
            System.out.println();

            System.out.println("\nMonster has its turn!");
            System.out.println();

            int random = (int) (Math.random() * 4 + 1);
            enemyInput = Integer.toString(random);


            switch (enemyInput) {
                case "1":
                    enemyDamage = enemyDamage + enemy.attack();
                    break;
                case "2":
                    enemyDamage = enemyDamage + enemy.specialAttack();
                    break;
                case "3":
                    enemy.healHP();
                    break;
                case "4":
                    enemy.healMP();
                    break;
            }


            //Player's health is deduced after enemy moves
            player.setHealth(player.getHealth() - enemyDamage);


        } while (enemy.isCharacterAlive() && player.isCharacterAlive());


        if (!enemy.isCharacterAlive()) {
            System.out.println("\nYou overwhelm your night terrors. You win, for now.");
            return 1;


        } else if (!player.isCharacterAlive()) {
            System.out.println("\nYou have been consumed by your fear, and you never wake up again.");

            gameOver();

            // implement game over
            return -1;
        }

        return -1;

    }

    public void gameOver()
    {
        System.out.println("\n    You Died\n" +
                "           _____   _____\n" +
                "          /     \\ /     \\\n" +
                "     ,   |       '       |\n" +
                "     I __L________       L__\n" +
                "O====IE__________/     ./___>\n" +
                "     I      \\.       ./\n" +
                "     `        \\.   ./\n" +
                "                \\ /\n" +
                "                 '\n");


        System.out.println("Good luck next time!");
        System.exit(1);

    }


}

