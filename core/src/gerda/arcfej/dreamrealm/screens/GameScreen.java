package gerda.arcfej.dreamrealm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import gerda.arcfej.dreamrealm.GameCore;
import gerda.arcfej.dreamrealm.map.Map;

/**
 *
 */
public class GameScreen extends AbstractFixSizedScreen {

    /**
     * The map on the Game Screen
     */
    private Map map;

    /**
     * The area the map will be drawn in
     */
    private Image mapArea;

    /**
     * Width of the map on the screen
     */
    private final int mapAreaWidth = 675;

    /**
     * Height of the map on the screen
     */
    private final int mapAreaHeight = 675;

    public GameScreen(GameCore gameCore, SpriteBatch batch) {
        super(gameCore, batch);

        map = new Map(batch);

        // Create layout
        // Root
        Table root = new Table(gameCore.skin);
        root.setFillParent(true);
        stage.addActor(root);

        // Left menu
        Table leftMenu = new Table(gameCore.skin);
        TextButton generate = new TextButton("Generate Map", gameCore.skin);
        generate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                map = new Map(batch);
            }
        });
        leftMenu.add(generate);
        root.add(leftMenu);

        mapArea = new Image(new Texture(new Pixmap(mapAreaWidth, mapAreaHeight, Pixmap.Format.Alpha)));
        root.add(mapArea);

        // Right menu
        Table rightMenu = new Table(gameCore.skin);
        rightMenu.add(new TextButton("Right Test", gameCore.skin));
        root.add(rightMenu);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // Clear the screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw layout with buttons, menus, etc.
        stage.getViewport().apply();
        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.act();
        stage.draw();


        // Transform the map's actor's bottom-left coordinates to the stage's coordinate system
        Vector2 stageBottomLeft = mapArea.localToStageCoordinates(new Vector2(mapArea.getX(), mapArea.getY()));
        // Transform it further to screen coordinates
        Vector3 screenBottomLeft = stage.getCamera().project(new Vector3(stageBottomLeft.x, stageBottomLeft.y, 0f));

        // Do the same with the top-right coordinates
        Vector2 stageTopRight = mapArea.localToStageCoordinates(new Vector2(mapArea.getX() + mapArea.getWidth(), mapArea.getY() + mapArea.getHeight()));
        Vector3 screenTopRight = stage.getCamera().project(new Vector3(stageTopRight.x, stageTopRight.y, 0f));

        // Draw map
        map.setScreenBounds(MathUtils.round(screenBottomLeft.x),
                MathUtils.round(screenBottomLeft.y),
                MathUtils.round(screenTopRight.x - screenBottomLeft.x),
                MathUtils.round(screenTopRight.y - screenBottomLeft.y));
        map.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height);
        map.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
    }

    // Former game menu
    /*
    /**
     * Game menu, which is displayed after loading a game or starting a new one
     * /
    public void gameMenu()
    {
        Scanner s = new Scanner(System.in);
        String input;

        do {

            System.out.println("\nPlease choose one of the following options:");
            System.out.println("1. Make a move");
            System.out.println("2. Save the game progress");
            System.out.println("3. Check your health status");
            System.out.println("4. Check your inventory");
            System.out.println("5. Check for traps");
            System.out.println("0. Exit the game");

            input = s.nextLine();

            switch(input)
            {
                case "1":
                {
                    boolean canMove = false;
                    Scanner sc = new Scanner(System.in);
                    String choice;


                    do {

                        if(gameScreen.getPlayer().getOil() < 5)
                        {
                            System.out.println("You have run out of oil. The darkness consumes you.");
                            gameScreen.gameOver();
                        }
                        gameScreen.displayMap();
                        System.out.println("\nChoose your direction of floating:");

                        System.out.println("u   - to go up");
                        System.out.println("d   - to go down");
                        System.out.println("l   - to go left");
                        System.out.println("r   - to go right");

                        System.out.println("\nEach turn consumes oil. You have " + gameScreen.getPlayer().getOil() +" oil left. Make sure it " +
                                "doesn't run out, otherwise the nightmares might end you.");

                        System.out.println("0   - to return");

                        choice = s.nextLine();
                        if(choice.equals("0")) break;
                        canMove = gameScreen.movePlayer(choice);

                        if(canMove)
                        {
                            gameScreen.getPlayer().setOil(gameScreen.getPlayer().getOil() - 5);
                            System.out.println("You have moved.\n");
                            gameScreen.displayMap();
                        }
                        else
                        {
                            System.out.println("Moving in that direction is forbidden!");
                        }

                    } while (!canMove);

                    break;
                }
                case "2":
                    saveGame();
                    break;
                case "3":
                {
                    System.out.println("You closely focus on your heartbeat.\n");
                    System.out.println("Your status:");

                    gameScreen.getPlayer().checkStatus();
                    break;
                }
                case "4":
                {
                    System.out.println("\nHere is your inventory. Defeat your nightmares or trade to acquire more items.\n");
                    gameScreen.getPlayer().getInv().showInventory();break;
                }

                case "5":
                {


                    boolean hasRock = gameScreen.getPlayer().getInv().findInInventory("Rock");
                    boolean trapExists = false;
                    if(hasRock)
                    {
                        gameScreen.disableTrap();
                        trapExists = gameScreen.getPlayer().getInv().remove("Rock");

                        if(trapExists)
                        {
                            System.out.println("Good job! You have successfully disabled a deadly trap.");

                        }
                        else
                        {
                            System.out.println("There were no traps found. However, you have wasted your rock.");
                        }

                    }
                    else
                    {
                        System.out.println("You don't have enough rocks to perform this action.");
                    }
                    break;
                }

                case "0": System.out.println("Thank you for playing! See you soon."); System.exit(1);
                default: System.out.println("Your input is invalid! Please enter an integer from 0 to 5.");
            }


        } while(!(input.equals("0")));
    }
    */

    // Variables
    /*
    private Interactive tempInteractive;

    private Queue<Enemy> respawnQueue;

    boolean firstTrap = true;
    boolean firstMonster = true;
    boolean firstNPC = true;
     */

    // Constructor
    /*
    public GameScreen()
    {
        respawnQueue = new LinkedList<>();
        respawnQueue.add(null);
        respawnQueue.add(null);
        respawnQueue.add(null);
        respawnQueue.add(null);
        respawnQueue.add(null);

        tempInteractive = null;

        generateExit();
    }
    */

    // Generate exit
    /*
    private void generateExit() {
        boolean top = player.getY() % (width / 2) == 0;
        int x = rnd.nextInt(width);
        if (top) {
            map[x][0].setUp(true);
        } else {
            map[x][height - 1].setDown(true);
        }
    }
     */

    // Display map
    /*
    public void displayMap() {
        for (int j = player.getY() - viewDistance; j <= player.getY() + viewDistance; j++) {
            displayRowUpperBorders(j);
            displayDataRow(j);
            if (j == player.getY() + viewDistance || j == height - 1) {
                displayRowBottomBorders(j);
            }
        }
    }

    private void displayDataRow(int row) {
        for (int j = 0; j < 3; j++) {
            String line = "";
            for (int i = player.getX() - viewDistance; i <= player.getX() + viewDistance; i++) {
                Field field;
                String[] side;
                String[] middle;
                try {
                    field = map[i][row];
                    side = field.canLeft() ? NO_WALL : VERTICAL_WALL;
                    middle = getInteractiveChar(field.getInteractive());
                    line = line.concat(side[j] + middle[j]);
                    if (i == player.getX() + viewDistance || i == width - 1) {
                        line = line.concat(field.canRight() ? NO_WALL[j] : VERTICAL_WALL[j]);
                    }
                } catch (IndexOutOfBoundsException e) {
                    line = line.concat(outsideMazeCell());
                }
            }
            System.out.println(line);
        }
    }

    private void displayRowUpperBorders(int row) {
        for (int i = player.getX() - viewDistance; i <= player.getX() + viewDistance; i++) {
            try {
                Field field = map[i][row];
                String side = VERTICAL_WALL[1];
                String middle = field.canUp() ? EMPTY_FIELD[1] : HORIZONTAL_WALL;
                System.out.print(side + middle);
                if (i == player.getX() + viewDistance || i == width - 1) {
                    System.out.print(VERTICAL_WALL[1]);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.print(outsideMazeCell());
            }
        }
        System.out.println();
    }

    private void displayRowBottomBorders(int row) {
        for (int i = player.getX() - viewDistance; i <= player.getX() + viewDistance; i++) {
            try {
                Field field = map[i][row];
                String side = VERTICAL_WALL[1];
                String middle = field.canDown() ? EMPTY_FIELD[1] : HORIZONTAL_WALL;
                System.out.print(side + middle);
                if (i == player.getX() + viewDistance || i == width - 1) {
                    System.out.print(VERTICAL_WALL[1]);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.print(outsideMazeCell());
            }
        }
        System.out.println();
    }

    private String outsideMazeCell() {
        return StringUtils.repeat(OUTSIDE_MAZE, EMPTY_FIELD[1].length() + VERTICAL_WALL[1].length());
    }
     */


//    private String[] getInteractiveChar(Interactive interactive) {
//        if (interactive instanceof Player) {
//            return PLAYER;
//        } else if (interactive instanceof Enemy) {
//            if (((Enemy) interactive).isCharacterAlive()) {
//                return ENEMY;
//            } else {
//                return DEAD_ENEMY;
//            }
//        } else if (interactive instanceof NPC) {
//            return NPC;
//        } else if (interactive instanceof Trap) {
//            return TRAP;
//        } else {
//            return EMPTY_FIELD;
//        }
//    }
//
//    public Player getPlayer() {
//        return player;
//    }
//
//    /**
//     * Displays the trade log when the player encounters a NPC and chooses the trade option.
//     */
//    public void showTradeLog(NPC npc) {
//        System.out.println("You have opened the trade log. You can buy valuable items that will aid you in your journey");
//        System.out.println("You can also sell items from your inventory. However, note that you cannot re-buy them.\n");
//
//        System.out.println("Trade log open!\n");
//
//        Scanner s = new Scanner(System.in);
//        String input;
//
//        //Looped until user exits
//        do {
//            System.out.println("Your inventory:\n");
//            player.getInv().showInventory();
//            System.out.println("You currently have " + player.getCurrency() + " coins.\n");
//
//            System.out.println(npc.getName() + "'s inventory:");
//            npc.getInv().showInventory();
//
//            System.out.println("Do you want to buy or sell items?\n");
//            System.out.println("1. Buy");
//            System.out.println("2. Sell");
//            System.out.println("0. Leave");
//
//
//            System.out.print("Please choose your option:");
//            //Input is scanned
//            input = s.nextLine();
//
//            switch (input) {
//                case "1":
//                    buyItems(npc);
//                    break;
//                case "2":
//                    sellItems(npc);
//                    break;
//                case "0":
//                    System.out.println("You have opted to return to your adventure.");
//                    return;
//                default:
//                    System.out.println("Your choice is not valid. Please try again!");
//            }
//
//        } while (!(input.equals("0")));
//
//    }
//
//    /**
//     * Method for selling items. This option is selected from the trade log.
//     */
//    public void sellItems(NPC npc) {
//
//        do {
//
//            System.out.println("\nPlease state what item you want to sell, or enter 'return' to return:");
//            player.getInv().showInventory();
//
//            Scanner s = new Scanner(System.in);
//            String input;
//
//            input = s.nextLine();
//            if(input.equals("return")) return;
//            //If item is found in inventory
//            if (player.getInv().findInInventory(input))
//            {
//                player.setCurrency(player.getCurrency() + player.getInv().returnItem(input).getPrice());
//                player.getInv().remove(input);
//                npc.getInv().addToInventory(input);
//                System.out.println("\nYou currently have: " + player.getCurrency() + " coins.");
//            }
//            else
//            {
//                //If item doesn't exist
//                System.out.println("Item not found! Try again");
//            }
//
//            // Additional menu, restarts if input is wrong
//            do {
//
//                System.out.println("\nDo you want to sell more items?\n");
//                System.out.println("1. Yes, I have goods to provide");
//                System.out.println("2. No, take me back to the trade log");
//
//                input = s.nextLine();
//                switch (input)
//                {
//                    case "1": break;
//                    case "2": return;
//                    default: System.out.println("Invalid option! Please choose an integer 1 or 2.");
//                }
//
//            } while (!(input.equals("1")));
//
//        } while (true);
//
//
//    }
//
//    /**
//     * Method for buying items. This option is accessible from the trade log.
//     */
//    public void buyItems(NPC npc)
//    {
//        do {
//            System.out.println("\nPlease state what item you want to buy, or enter 'return' to return:");
//
//            Scanner s = new Scanner(System.in);
//            String input;
//
//            input = s.nextLine();
//            if(input.equals("return")) return;
//            //If item is found in NPC inventory
//            if (npc.getInv().findInInventory(input))
//            {
//                //Checks if player can afford the item
//                if(player.getCurrency() >= npc.getInv().returnItem(input).getPrice())
//                {
//                    player.setCurrency(player.getCurrency() - npc.getInv().returnItem(input).getPrice());
//                    npc.getInv().remove(input);
//                    player.getInv().addToInventory(input);
//                    System.out.println("\nYou currently have: " + player.getCurrency() + " coins.");
//                }
//                else
//                {
//                    System.out.println("You do not have enough coins to buy this item.");
//                }
//
//            }
//            else
//            {
//                //If item doesn't exist
//                System.out.println("Item not found! Try again");
//            }
//
//            // Additional menu, restarts if input is wrong
//            do {
//
//                System.out.println("\nDo you want to buy more items?\n");
//                System.out.println("1. Yes, I'm interested in more goods");
//                System.out.println("2. No, take me back to the trade log");
//
//                input = s.nextLine();
//                switch (input)
//                {
//                    case "1": break;
//                    case "2": return;
//                    default: System.out.println("Invalid option! Please choose an integer 1 or 2.");
//                }
//
//            } while (!(input.equals("1")));
//        } while (true);
//    }
//
//    /**
//     * Moves player in a selected location
//     * @param direction - input taken from the user
//     * @return true/false depending on whether the player can move
//     */
//    public boolean movePlayer(String direction) {
//        int playerX = player.getX();
//        int playerY = player.getY();
//
//        int sizeOfField = 1;
//
//        int newX=0;
//        int newY=0;
//
//        try {
//            //up
//            switch (direction) {
//                case "u":
//                    if (map[playerX][playerY].canUp()) {
//
//                            newX = playerX;
//                            newY = playerY - sizeOfField;
//
//
//                    } else {
//                        return false;
//                    }
//                    break;
//                //left
//                case "l":
//                    if (map[playerX][playerY].canLeft()) {
//                        newX = playerX - sizeOfField;
//                        newY = playerY;
//                    } else {
//                        return false;
//                    }
//                    break;
//                    //right
//                case "r":
//                    if (map[playerX][playerY].canRight()) {
//                        newX = playerX + sizeOfField;
//                        newY = playerY;
//                    } else {
//                        return false;
//                    }
//                    break;
//                    //down
//                case "d":
//                    if (map[playerX][playerY].canDown()) {
//                        newX = playerX;
//                        newY = playerY + sizeOfField;
//                    } else {
//                        return false;
//                    }
//                    break;
//                default:
//                    //Invalid input therefore cannot move
//                    return false;
//            }
//            try {
//                Field field = map[newX][newY];
//            } catch (ArrayIndexOutOfBoundsException e) {
//                win();
//            }
//            //Puts down the old interactive
//            if (tempInteractive != null && !(tempInteractive.equals(player))) {
//                map[tempInteractive.getX()][tempInteractive.getY()].setInteractive(tempInteractive);
//                tempInteractive = null;
//            } else {
//                //Sets old position to have no interaction
//                map[playerX][playerY].setInteractive(null);
//            }
//
//            //Saves old interactive
//            tempInteractive = map[newX][newY].getInteractive();
//
//            //Sets new position of the player
//            player.setPosition(newX, newY);
//
//            //Sets new player position to have the marker of the player
//            map[newX][newY].setInteractive(player);
//
//            Enemy deadEnemy = null;
//            if (tempInteractive instanceof Enemy && ((Enemy) tempInteractive).isCharacterAlive()) {
//                // Add enemy to respawn queue if dead
//                deadEnemy = combat(newX, newY) == 1 ? (Enemy) tempInteractive : null;
//            }
//            if(tempInteractive instanceof Trap)
//            {
//                if(firstTrap = true) encounterTrap1();
//
//                else gameOver();
//            }
//            if(tempInteractive instanceof NPC)
//            {
//                if(firstNPC) interaction1(newX, newY);
//                else
//                {
//                    System.out.println("\tHello, wanderer. I hope you are well, although, by looking at you I can assume that you have " +
//                            "\nencountered some of this place’s nightmarish creations. But you are alive! Should not be that gloomy," +
//                            " \nenjoy your life as long as you can. In the meantime, would you like to buy anything?");
//                    npcInteract(newX, newY);
//                }
//
//            }
//            // Add the enemy which dead this turn (or null) to the respawn queue
//            respawnQueue.add(deadEnemy);
//            // respawn the enemies dead for the defined turns or do nothing
//            Enemy resurrect = respawnQueue.remove();
//            if (resurrect != null) resurrect.setHealth(Character.MAX_HEALTH);
//
//            return true;
//        } catch (ArrayIndexOutOfBoundsException e) {
//
//            //If out of boundaries cannot move
//            return false;
//        }
//    }
//
//    /**
//     * first npc interaction
//     */
//    private void interaction1(int x, int y) {
//
//        System.out.println("\nAfter a while you notice a silhouette in front of you. Hopefully it is someone that could tell you anything about this odd place." +
//                " \nShouldn’t assume anything negative, even if they look super creepy, right?\n" +
//
//                "\n'Oh, so you were what I assumed was a firefly. How do you find yourself in this peculiar place? Mh, you are not really talkative, " +
//                "\nare you? Well, then. Welcome to Dream Realm. As you have already noticed, it is really dark. You might want to keep that lamp on," +
//                " \ncause the creatures that live in here might want to feed on you when it goes off. Don’t worry though. Just remember to add some" +
//                " \noil when you find it. If you’d like to, I can sell you some.' \n");
//
//        npcInteract(x, y);
//        firstNPC = false;
//    }
//
//    /**
//     * Handles the interaction with the NPC.
//     * @param x
//     * @param y
//     */
//    private void npcInteract(int x, int y)
//    {
//        NPC npc = (NPC) tempInteractive;
//
//        Scanner sc = new Scanner(System.in);
//        npc.getInv().resetInventory();
//        npc.getInv().addToInventory("Oil");
//        npc.getInv().addToInventory("Oil");
//        npc.getInv().addToInventory("Rock");
//        npc.getInv().addToInventory("Rock");
//        npc.getInv().addToInventory("Liquid light");
//        npc.getInv().addToInventory("Liquid light");
//        npc.getInv().addToInventory("Dreamcatcher");
//        npc.getInv().addToInventory("Dreamcatcher");
//        String choice, choice2;
//
//        System.out.println("\n" +
//                "1.\tTrade\n" +
//                "2.\tLeave\n" +
//                "3.\tCan I ask you something?");
//        choice = sc.nextLine();
//        switch(choice)
//        {
//            case "1": showTradeLog(npc);
//            case "2": return;
//            case "3":
//            {
//               boolean retry = true;
//                do {
//                    retry = true;
//
//                    System.out.println("a.	What is this place?");
//                    System.out.println("b.  How do I get out of here?");
//                    System.out.println("c.  What kind of creatures can I encounter here?");
//                    System.out.println("0.  Return");
//
//                    choice2 = sc.nextLine();
//
//                    switch(choice2)
//                    {
//                        case "a": System.out.println("'I do not think I can give you the exact answer. " +
//                                "It is incredibly bizarre, and thus, I think it cannot be defined. '");break;
//                        case"b": System.out.println("'You need to find your own way. Although, you might want to try and ask the others.' ");break;
//                        case"c": System.out.println("'Even though they have bodies, they are like nightmares. They will feed on your body and soul. " +
//                                "If you try and kill them, they may once again appear and hunt you.'");break;
//                        case"0": retry = false; continue;
//                        default: System.out.println("Invalid input. Try again!"); retry = true; break;
//                    }
//
//                } while (retry);
//            }
//        }
//    }
//
//    /**
//     * Plays when you step on a trap
//     */
//    public void encounterTrap1()
//    {
//        if(firstTrap)
//        {
//            Scanner sc = new Scanner(System.in);
//            String trapInput;
//
//            System.out.println("\nClick, click. What is this sound? It comes from the eastern side. The noise is quite funny. ");
//            System.out.println("Almost... Mechanical? You wonder why there would be anything machine-like. You turn to ");
//            System.out.println("examine the source of the sound. There is an enormous rusty machine standing in your way. ");
//            System.out.println("It does not look safe nor useful. It seems impossible to walk next to it, although, if you tried ");
//            System.out.println("you might get crushed by all the moving parts. Maybe you could try and destroy it, like you do ");
//            System.out.println("with everything in your life? You can spot some gears shining in the middle of it. Could a simple ");
//            System.out.println("rock and strength of your amazing muscles solve the problem?");
//
//            boolean retry = false;
//            do {
//
//                System.out.println("\n1.\tThrow a rock at it. What could go wrong?\n" +
//                        "2.\tSqueeze next to it. How hard could it be?\n");
//
//                trapInput = sc.nextLine();
//                switch (trapInput)
//                {
//                    case"1": retry = false; continue;
//                    case "2":
//                    {
//                        System.out.println("As you tried to squeeze next to the dangerously looking machine, " +
//                                "your clothes got into the motor and your body got shattered. Good job. ");
//
//                        gameOver();
//                    }
//                    default: System.out.println("Please check your input!"); retry=true;break;
//                }
//            } while (retry);
//
//            firstTrap = false;
//            System.out.println("\nThe rock hit the gears and the rusty parts fell apart, unblocking the path.");
//            System.out.println("\nNEXT TIME YOU STEP ON A TRAP, YOU WILL BE FATALLY INJURED. BE AWARE!");
//            System.out.println("USE THE CHECK FOR TRAP OPTION TO DISABLE THEM.\n");
//
//
//            disableTrap();
//            tempInteractive = null;
//
//        }
//
//    }
//
//
//    /**
//     * Disables traps if they exist
//     * @return false/true whether there were traps to disable
//     */
//    public boolean disableTrap()
//    {
//        int playerX = player.getX();
//        int playerY = player.getY();
//
//        boolean trapExists = false;
//
//        try {
//            if(map[playerX+1][playerY].canRight())
//            {
//                if(map[playerX+1][playerY].getInteractive() instanceof Trap)
//                {
//                    trapExists=true;
//                    map[playerX+1][playerY].setInteractive(null);
//                    tempInteractive = null;
//
//                }
//            }
//            if(map[playerX-1][playerY].canLeft() )
//            {
//                if(map[playerX-1][playerY].getInteractive() instanceof Trap)
//                {
//                    trapExists=true;
//                    map[playerX-1][playerY].setInteractive(null);
//                    tempInteractive = null;
//
//                }
//            }
//            if(map[playerX][playerY-1].canUp() )
//            {
//                if(map[playerX][playerY-1].getInteractive() instanceof Trap)
//                {
//                    trapExists=true;
//                    map[playerX][playerY-1].setInteractive(null);
//                    tempInteractive = null;
//
//                }
//            }
//            if(map[playerX][playerY-1].canDown())
//            {
//                if(map[playerX][playerY-1].getInteractive() instanceof Trap)
//                {
//                    trapExists=true;
//                    map[playerX][playerY-1].setInteractive(null);
//                    tempInteractive = null;
//
//                }
//
//            }
//        } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
//
//        }
//
//        return trapExists;
//    }
//
//    /**
//     * Displays the possible combat options for the player.
//     */
//    public void showCombatOptions()
//    {
//        System.out.println("\nRegular attacks\n");
//        System.out.println("1. Try to cut through the monster’s chest. Maybe it does have a heart on the contrary to you?");
//        System.out.println("2. Attempt to cut off the monster’s legs. Legless opponent sounds like something you could fight.");
//        System.out.println("3. Throw the sword at the monster's head hoping that you aim properly and won’t lose your weapon.");
//        System.out.println("4. Pirouette/Spin while blindly flinging the blade around like a sane person you are.");
//        System.out.println("5. Just try to harm the monster with the sword somehow - you are so scared that you can barely move.\n");
//        System.out.println("Special attacks that require your will power:\n");
//        System.out.println("6. Try to tickle it to death with hands you create with the power of your mind. You brute. ");
//        System.out.println("7. Create a fireball and throw it at the monster, might make it evaporate? Or create a sculpture out of it. That might be your chance to become a creepy artist. ");
//        System.out.println("8. Try and dismember the monster’s body parts with the power of your mind to show off how almighty you are at the moment and start to wonder who the real monster is right now.");
//        System.out.println("\nRestorative options:");
//        System.out.println("9. Use a dreamcatcher to grasp your breath - Healing ability");
//        System.out.println("10. Consume a liquid light potion to restore your will power - Restoration ability");
//        System.out.println("0. Run away, like the coward you are");
//
//    }
//
//    /**
//     * Combat method is brought up whenever
//     * the player faces an enemy.
//     * @param x
//     * @param y
//     * @return number depending on outcome: -1 for death,
//     * 0 for fleeing and 1 for victory.
//     */
//    public int combat(int x, int y) {
//        System.out.println("\nYou feel like you're being watched. You might not be ready, but you must face your night terrors.");
//        Scanner s = new Scanner(System.in);
//        Enemy enemy = (Enemy) tempInteractive;
//        String userInput;
//        String enemyInput;
//
//        //Records damage enemy and player do each turn
//        int enemyDamage = 0;
//        int playerDamage = 0;
//
//        //Retry if input is invalid
//        boolean retry;
//
//        do {
//
//            System.out.println("\nYour status:");
//            System.out.println("Your health: " + player.getHealth());
//            System.out.println("Your will power: " + player.getPower());
//            System.out.println();
//
//            enemyDamage = 0;
//            playerDamage = 0;
//
//            do {
//                System.out.println("Please choose your next move:");
//                showCombatOptions();
//
//                userInput = s.nextLine();
//                retry = false;
//
//                playerDamage = 0;
//                switch (userInput) {
//                    case "1": case "2": case "3": case"4": case"5":
//                        playerDamage = playerDamage + player.attack();
//                        break;
//                    case "6": case"7": case"8":
//                        playerDamage = playerDamage + player.specialAttack();
//                        break;
//                    case "9":
//                        player.healHP();
//                        break;
//                    case "10":
//                        player.healPW();
//                        break;
//                    case "0":
//                        player.flee();
//                        return 0;
//                    default:
//                        System.out.println("Please try again - wrong user input.");
//                        retry = true;
//                        break;
//                }
//
//            } while (retry);
//
//
//            //Enemy health is deduced after player moves
//            enemy.setHealth(enemy.getHealth() - playerDamage);
//            if (!enemy.isCharacterAlive()) break;
//
//            //Enemy has their turn
//            System.out.println("\nMonster status:");
//            System.out.println("Monster health: " + enemy.getHealth());
//            System.out.println("Monster will power: " + enemy.getPower());
//            ;
//            System.out.println();
//
//            System.out.println("\nMonster has its turn!");
//            System.out.println();
//
//            int random = (int) (Math.random() * 4 + 1);
//            enemyInput = Integer.toString(random);
//
//
//            switch (enemyInput) {
//                case "1":
//                    enemyDamage = enemyDamage + enemy.attack();
//                    break;
//                case "2":
//                    enemyDamage = enemyDamage + enemy.specialAttack();
//                    break;
//                case "3":
//                    enemy.healHP();
//                    break;
//                case "4":
//                    enemy.healPW();
//                    break;
//            }
//
//
//            //Player's health is deduced after enemy moves
//            player.setHealth(player.getHealth() - enemyDamage);
//
//
//        } while (enemy.isCharacterAlive() && player.isCharacterAlive());
//
//
//        if (!enemy.isCharacterAlive()) {
//            System.out.println("\nYou overwhelm your night terrors. You win, for now.");
//
//            int chance = rnd.nextInt(100);
//            if (chance < 15) {
//                player.getInv().addToInventory("Liquid light");
//            } else if (chance < 35) {
//                player.getInv().addToInventory("Dreamcathcer");
//            } else {
//                player.getInv().addToInventory("Rock");
//            }
//            player.addOil(20);
//
//            return 1;
//
//
//        } else if (!player.isCharacterAlive()) {
//            System.out.println("\nYou have been consumed by your fear, and you never wake up again.");
//
//            gameOver();
//
//            // implement game over
//            return -1;
//        }
//
//        return -1;
//
//    }
//
//    private void win() {
//        System.out.println("Congratulation! You won!");
//        System.exit(2);
//    }
//
//    /**
//     * Method that calls the game over screen.
//     */
//    public void gameOver()
//    {
//        System.out.println("\n  You Died.\n" +
//                "           _____   _____\n" +
//                "          /     \\ /     \\\n" +
//                "     ,   |       '       |\n" +
//                "     I __L________       L__\n" +
//                "O====IE__________/     ./___>\n" +
//                "     I      \\.       ./\n" +
//                "     `        \\.   ./\n" +
//                "                \\ /\n" +
//                "                 '\n");
//
//
//        System.out.println("Unfortunately, you never wake up again...");
//        System.out.println("Good luck next time!");
//        System.exit(1);
//
//    }
}
