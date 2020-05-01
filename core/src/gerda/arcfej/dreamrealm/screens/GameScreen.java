package gerda.arcfej.dreamrealm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.badlogic.gdx.utils.Predicate;
import com.badlogic.gdx.utils.Predicate.PredicateIterator;
import com.badlogic.gdx.utils.viewport.Viewport;
import gerda.arcfej.dreamrealm.GameCore;
import gerda.arcfej.dreamrealm.interactives.Player;
import gerda.arcfej.dreamrealm.map.Field;

import static com.badlogic.gdx.graphics.g2d.TextureAtlas.*;

/**
 *
 */
public class GameScreen extends AbstractFixSizedScreen {

    /**
     * Texture atlases
     */
    private TextureAtlas dungeon;
    private TextureAtlas interactives;
    private TextureAtlas enemyAtlas;

    /**
     * Textures to be used in the game.
     */
    private Array<AtlasRegion> walls;
    private Array<AtlasRegion> enemies;
    private AtlasRegion exit;
    private AtlasRegion pathTexture;
    private AtlasRegion playerTexture;
    private AtlasRegion deadEnemy;
    private AtlasRegion shop;
    private AtlasRegion trap;

    /**
     * The visual representation of the maze
     */
    private TiledMap map;

    /**
     * The camera the map is displayed through
     */
    private OrthographicCamera camera;

    /**
     * Width of the map on the screen
     */
    private final int cameraWidth = 675;

    /**
     * Height of the map on the screen
     */
    private final int cameraHeight = 675;

    /**
     * Responsible to render the map on the screen
     */
    private TiledMapRenderer mapRenderer;

    /**
     * For map generation
     */
    private int enemiesPercent= 12;
    private int shopsPercent = 6;
    private int trapsPercent = 8;
    private int mazeWidth = 10;
    private int mazeHeight = 10;
    private int tileSize = 320;
    // Number of tiles
    private int mapWidth = mazeWidth * 2 + 1;
    private int mapHeight = mazeHeight * 2 + 1;

    /**
     * For map displaying
     */
    private int viewDistance = 2;

    /**
     * The data representation of the maze
     */
    private Field[][] mazeData;

    /**
     * The player of the current game
     */
    private Player player;

    public GameScreen(GameCore gameCore, SpriteBatch batch) {
        super(gameCore, batch);
        // Load textures
        dungeon = new TextureAtlas("texture_atlases/dungeon.atlas");
        walls = new Array<>(3);
        walls.add(dungeon.findRegion("stone-wall"));
        walls.add(dungeon.findRegion("brick-wall"));
        walls.add(dungeon.findRegion("broken-wall"));

        exit = dungeon.findRegion("dungeon-gate");
        pathTexture = dungeon.findRegion("path-tile");

        interactives = new TextureAtlas("texture_atlases/interactives.atlas");
        playerTexture = interactives.findRegion("player");
        deadEnemy = interactives.findRegion("dead_enemy");
        shop = interactives.findRegion("shop");
        trap = interactives.findRegion("trap");

        enemyAtlas = new TextureAtlas("texture_atlases/enemies.atlas");
        enemies = enemyAtlas.getRegions();

        player = new Player(mazeWidth * mazeHeight / 2);
        // Place the player on the map at a random place
        player.setPosition(MathUtils.random(mazeWidth - 1), MathUtils.random(mazeHeight - 1));

        // Create the map and its layers
        map = new TiledMap();
        createMapLayers();

        mazeData = new Field[mazeWidth][mazeHeight];
        generateMaze();

/*        enemies[0] = new Texture(Gdx.files.internal("enemies/bully-minion"));
        enemies[1] = new Texture(Gdx.files.internal("enemies/ceiling-barnacle"));
        enemies[2] = new Texture(Gdx.files.internal("enemies/evil-bat"));
        enemies[3] = new Texture(Gdx.files.internal("enemies/evil-minion"));
        enemies[4] = new Texture(Gdx.files.internal("enemies/gargoyle"));
        enemies[5] = new Texture(Gdx.files.internal("enemies/gooey-daemon"));
        enemies[6] = new Texture(Gdx.files.internal("enemies/grim-reaper"));
        enemies[7] = new Texture(Gdx.files.internal("enemies/ice-golem"));
        enemies[8] = new Texture(Gdx.files.internal("enemies/minotaur"));
        enemies[9] = new Texture(Gdx.files.internal("enemies/shambling-mound"));
        enemies[10] = new Texture(Gdx.files.internal("enemies/skeleton"));
        enemies[11] = new Texture(Gdx.files.internal("enemies/spectre"));
        enemies[12] = new Texture(Gdx.files.internal("enemies/troglodyte"));
        enemies[13] = new Texture(Gdx.files.internal("enemies/werewolf"));
*/

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
                generateMaze();
            }
        });
        leftMenu.add(generate);
        root.add(leftMenu);

        // Map
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        // Set screen ratio
        if (h > w) {
            camera = new OrthographicCamera(cameraWidth, cameraHeight * (h / w));
        } else {
            camera = new OrthographicCamera(cameraWidth * w / h, cameraHeight);
        }
        // Set zoom based on view distance
        camera.zoom = tileSize / camera.viewportHeight * 21/*(viewDistance * 2 + 1)*/;
        // Center the map on the player
        // TODO uncomment this and comment out the other
//        camera.position.set((player.getX() * 2 + 1) * tileSize + tileSize / 2f,
//                (player.getY() * 2 + 1) * tileSize + tileSize / 2f,
//                0);
        camera.position.set(mapWidth * tileSize / 2f,
                mapHeight * tileSize / 2f,
                0);
        camera.update();
        mapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        root.add(new Image(new Texture(new Pixmap(cameraWidth, cameraHeight, Pixmap.Format.Alpha))));

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

        stage.act();
        stage.draw();

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        dungeon.dispose();
        interactives.dispose();
        enemyAtlas.dispose();
    }

    private void createMapLayers() {
        MapLayers layers = map.getLayers();
        TiledMapTileLayer background = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        background.setName("background");
        TiledMapTileLayer dungeon = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        dungeon.setName("dungeon");
        TiledMapTileLayer interactives = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        interactives.setName("interactives");
        TiledMapTileLayer player = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        player.setName("player");

        layers.add(background);
        layers.add(dungeon);
        layers.add(interactives);
        layers.add(player);
    }

    // Maze generation
    // Uses Prim's algorithm to generate a map. All the edges have the same weight, so the next neighbour are chosen randomly.
    private void generateMaze() {
        // A 2D array of fields which will become a maze.
        // Fill it up with empty fields, non of them part of the actual maze yet.
        for (int i = 0; i < mazeWidth; i++) {
            for (int j = 0; j < mazeHeight; j++) {
                Field field = new Field();
                mazeData[i][j] = field;
                field.setPosition(i, j);
            }
        }

        // A set of fields which are part of a maze. Empty before the maze generation.
        ObjectSet<Field> inside = new ObjectSet<>(mazeWidth * mazeHeight);
        // A set of fields at the edge of the already generated maze. (Neighbour fields around the maze)
        // It will become empty at the end of the generation, when all the map's fields will be part of the maze.
        OrderedSet<Field> neighbours = new OrderedSet<>(mazeWidth * mazeHeight / 2);

        // Start the maze generation from a random point and add it to the maze (in) as its first field
        int x = MathUtils.random(mazeWidth - 1);
        int y = MathUtils.random(mazeHeight - 1);
        Field newPath = mazeData[x][y];
        inside.add(newPath);
        do {
            // An array of fields adjacent to the previous new path (max 4)
            Array<Field> adjacents = new Array<>(false, 4);
            // Try to add all the adjacent fields to the array. IndexOutOfBound exceptions are ignored.
            try {
                adjacents.add(mazeData[x - 1][y]);
            } catch (Exception ignore) { }
            try {
                adjacents.add(mazeData[x + 1][y]);
            } catch (Exception ignore) { }
            try {
                adjacents.add(mazeData[x][y - 1]);
            } catch (Exception ignore) { }
            try {
                adjacents.add(mazeData[x][y + 1]);
            } catch (Exception ignore) { }
            // Add the new path's adjacent fields to the neighbours of the maze, if they're not already part of the maze.
            for (Field next : adjacents) {
                if (!inside.contains(next)) {
                    neighbours.add(next);
                }
            }
            Field previousPath = newPath;

            // Choose one random neighbour as the next new path
            newPath = neighbours.removeIndex(MathUtils.random(neighbours.size - 1));
            x = newPath.getX();
            y = newPath.getY();

            // Predicates to determine if a field is left, right, up or down to the newly added path.
            Field finalNewPath = newPath;
            Predicate<Field> isLeft = field -> field.getY() == finalNewPath.getY() && field.getX() + 1 == finalNewPath.getX();
            Predicate<Field> isRight = field -> field.getY() == finalNewPath.getY() && field.getX() - 1 == finalNewPath.getX();
            Predicate<Field> isUp = field -> field.getX() == finalNewPath.getX() && field.getY() - 1 == finalNewPath.getY();
            Predicate<Field> isDown = field -> field.getX() == finalNewPath.getX() && field.getY() + 1 == finalNewPath.getY();
            // Combine these predicates
            Predicate<Field> isNeighbour = field ->
                    isLeft.evaluate(field) || isRight.evaluate(field) || isUp.evaluate(field) || isDown.evaluate(field);

            // Find a random neighbour (the first one)
            PredicateIterator<Field> neighbourFilter = new PredicateIterator<>(inside, isNeighbour);
            Field connect = neighbourFilter.next();

            // Open the connection between the new path and (one of) the neighbouring maze cell then add it to the maze
            if (isUp.evaluate(connect)) {
                connect.setDown(true);
                newPath.setUp(true);
            } else if (isDown.evaluate(connect)) {
                connect.setUp(true);
                newPath.setDown(true);
            } else if (isRight.evaluate(connect)) {
                connect.setLeft(true);
                newPath.setRight(true);
            } else if (isLeft.evaluate(connect)) {
                connect.setRight(true);
                newPath.setLeft(true);
            }
            inside.add(newPath);
        } while (!neighbours.isEmpty()); // Run until there's no more empty fields left

        // Set the graphics
        TiledMapTileLayer dungeon = (TiledMapTileLayer) map.getLayers().get("dungeon");
        // Create the bottom and left side of the maze, full of walls
        for (y = 0; y < dungeon.getHeight(); y++) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(new StaticTiledMapTile(walls.get(MathUtils.random(walls.size - 1))))
                    .setRotation(TiledMapTileLayer.Cell.ROTATE_270);
            dungeon.setCell(0, y, cell);
        }
        for (x = 1; x < dungeon.getWidth(); x++) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(new StaticTiledMapTile(walls.get(MathUtils.random(walls.size - 1))));
            dungeon.setCell(x, 0, cell);
        }

        // Go through the maze and fill the map up with walls
        for (x = 0; x < mazeData.length; x++) {
            Field[] row = mazeData[x];
            for (y = 0; y < row.length; y++) {
                Field path = row[y];

                // Set texture on the path
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(pathTexture));
                dungeon.setCell(x * 2 + 1, y * 2 + 1, cell);

                // Set a wall or path on the top
                TiledMapTileLayer.Cell up = new TiledMapTileLayer.Cell();
                up.setTile(new StaticTiledMapTile(
                        path.canUp() ? pathTexture : walls.get(MathUtils.random(walls.size - 1))));
                dungeon.setCell(x * 2 + 1, y * 2 + 2, up);

                // Set a wall or path on the right
                TiledMapTileLayer.Cell right = new TiledMapTileLayer.Cell();
                right.setTile(new StaticTiledMapTile(
                        path.canRight() ? pathTexture : walls.get(MathUtils.random(walls.size - 1))));
                // Randomize rotation on the right
                right.setRotation(MathUtils.randomBoolean() ? TiledMapTileLayer.Cell.ROTATE_90 :TiledMapTileLayer.Cell.ROTATE_270);
                dungeon.setCell(x * 2 + 2, y * 2 + 1, right);

                // Set the wall in the top-right corner to the path
                TiledMapTileLayer.Cell upRight = new TiledMapTileLayer.Cell();
                upRight.setTile(new StaticTiledMapTile(walls.get(MathUtils.random(walls.size - 1))));
                upRight.setRotation(MathUtils.randomBoolean() ? TiledMapTileLayer.Cell.ROTATE_90 :TiledMapTileLayer.Cell.ROTATE_270);
                dungeon.setCell(x * 2 + 2, y * 2 + 2, upRight);
            }
        }
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

        map[player.getX()][player.getY()].setInteractive(player);
        generateMapItems();
        generateExit();
    }
    */

    // Generate map items
    /*
    private int enemyNum;
    private int npcNum;
    private int trapNum;

    private void generateMapItems() {
        float max = (width % 2 == 0 ? width : width + 1) * (height % 2 == 0 ? height : height + 1);
        enemyNum = (int) Math.round(max * ENEMIES_PERCENT / 100);
        npcNum = (int) Math.round(max * NPCS_PERCENT / 100);
        trapNum = (int) Math.round(max * TRAPS_PERCENT / 100);

        for (int i = 0; i < width; i += 2) {
            for (int j = 0; j < height; j += 2) {
                placeInteractive(
                        i,
                        Math.min(i + 2, width),
                        j,
                        Math.min(j + 2, height)
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

        int errorCount = 0;

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
            errorCount++;
            if (errorCount > 10) {
                break;
            }
        }
        if (errorCount <= 10) {
            interactive.setPosition(x, y);
        }
        map[x][y].setInteractive(interactive);
        if (interactive instanceof Enemy) enemyNum--;
        else if (interactive instanceof NPC) npcNum--;
        else if (interactive instanceof Trap) trapNum--;
    }

    private boolean isPlacementAllowed(Interactive interactive, int x, int y) {
        if (interactive == null) {
            return false;
        }
        int count = 0;
        for (int i = (x == 0 ? x : x - 1); i <= (x >= width ? width - 1 : x); i++) {
            for (int j = (y == 0 ? y : y - 1); j <= (j >= height ? height - 1 : j); j++) {
                Interactive existing = map[i][j].getInteractive();
                if (existing != null) {
                    count++;
                    if (interactive.getClass().equals(existing.getClass())) {
                        return false;
                    }
                }
            }
        }
        return count < 2;
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
