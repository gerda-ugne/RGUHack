package gerda.arcfej.dreamrealm.map;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import gerda.arcfej.dreamrealm.map.interactives.Player;
import gerda.arcfej.dreamrealm.map.interactives.Shop;
import gerda.arcfej.dreamrealm.map.interactives.Trap;
import gerda.arcfej.dreamrealm.screens.GameScreen;

public class Map extends Stage implements Disposable {

    /**
     * Texture atlases
     */
    private TextureAtlas dungeon;
    private TextureAtlas interactives;
    private TextureAtlas enemyAtlas;

    /**
     * Textures to be used in the game.
     */
    private Array<TextureAtlas.AtlasRegion> walls;
    private Array<TextureAtlas.AtlasRegion> enemies;
    private TextureAtlas.AtlasRegion exit;
    private TextureAtlas.AtlasRegion pathTexture;
    private TextureAtlas.AtlasRegion playerTexture;
    private TextureAtlas.AtlasRegion deadEnemy;
    private TextureAtlas.AtlasRegion shop;
    private TextureAtlas.AtlasRegion trap;

    /**
     * The visual representation of the maze
     */
    private TiledMap map;

    /**
     * Responsible to render the map on the screen
     */
    private TiledMapRenderer renderer;

    /**
     * For map generation
     */
    private int enemiesPercent= 12;
    private int shopsPercent = 6;
    private int trapsPercent = 8;
    private int mazeWidth = 20;
    private int mazeHeight = 20;
    private int tileSize = 320;
    // Number of tiles
    // The Map class using this coordinate system
    // TODO merge the two coordinate system
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
    // TODO extact it to GameScreen somehow?
    private Player player;

    /**
     * The cell on the TiledMap which represents the player.
     */
    private Cell playerCell;

    public Map(Batch batch) {
        super(new ExtendViewport(0, 0), batch);
        ((ExtendViewport) getViewport()).setMinWorldHeight(mapHeight);
        ((ExtendViewport) getViewport()).setMinWorldWidth(mapWidth);

        loadTextures();

        map = new TiledMap();
        setDebugAll(true);
        createMapLayers();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth, mapHeight);

        renderer = new OrthogonalTiledMapRenderer(map, 1f / tileSize, batch);
        renderer.setView(camera);

        getViewport().setCamera(camera);

        mazeData = new Field[mazeWidth][mazeHeight];
        generateMaze();

        // Place the player on the map at a random place
        player = new Player(mazeWidth * mazeHeight / 2);
        player.setPosition(MathUtils.random(mazeWidth - 1), MathUtils.random(mazeHeight - 1));
        mazeData[player.x][player.y].setInteractive(player);
        playerCell = new Cell();
        playerCell.setTile(new StaticTiledMapTile(playerTexture));
        ((TiledMapTileLayer) map.getLayers().get("player"))
                .setCell(player.x * 2 + 1, player.y * 2 + 1, playerCell);

        generateInteractives();
        generateExit();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Actor actor = new Actor();
                actor.setBounds(x, y, 1, 1);
                int finalX = x;
                int finalY = y;
                actor.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameScreen.setDisplayedText(finalX + ", " + finalY + " has clicked");
                    }
                });
                addActor(actor);
            }
        }
    }

    // INITIALIZATION

    private void loadTextures() {
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

        /*
        enemies[0] = new Texture(Gdx.files.internal("enemies/bully-minion"));
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
    }

    private void createMapLayers() {
        // TODO global variables
        TiledMapTileLayer background = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        background.setName("background");
        TiledMapTileLayer dungeon = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        dungeon.setName("dungeon");
        TiledMapTileLayer interactives = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        interactives.setName("interactives");
        TiledMapTileLayer player = new TiledMapTileLayer(mapWidth, mapHeight, tileSize, tileSize);
        player.setName("player");

        MapLayers layers = map.getLayers();
        // Delete all the layers first
        for (int i = layers.size() - 1; i >= 0; i--) {
            layers.remove(i);
        }

        layers.add(background);
        layers.add(dungeon);
        layers.add(interactives);
        layers.add(player);
    }

    // GENERALIZATION

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
        // An array of fields adjacent to the previous new path (max 4)
        Array<Field> adjacents = new Array<>(false, 4);

        // Start the maze generation from a random point and add it to the maze (in) as its first field
        int x = MathUtils.random(mazeWidth - 1);
        int y = MathUtils.random(mazeHeight - 1);
        Field newPath = mazeData[x][y];
        inside.add(newPath);
        do {
            adjacents.clear();
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
            x = newPath.x;
            y = newPath.y;

            // Predicates to determine if a field is left, right, up or down to the newly added path.
            Field finalNewPath = newPath;
            Predicate<Field> isLeft = field -> field.y == finalNewPath.y && field.x + 1 == finalNewPath.x;
            Predicate<Field> isRight = field -> field.y == finalNewPath.y && field.x - 1 == finalNewPath.x;
            Predicate<Field> isUp = field -> field.x == finalNewPath.x && field.y - 1 == finalNewPath.y;
            Predicate<Field> isDown = field -> field.x == finalNewPath.x && field.y + 1 == finalNewPath.y;
            // Combine these predicates
            Predicate<Field> isNeighbour = field ->
                    isLeft.evaluate(field) || isRight.evaluate(field) || isUp.evaluate(field) || isDown.evaluate(field);

            // Find a random neighbour (the first one)
            Predicate.PredicateIterator<Field> neighbourFilter = new Predicate.PredicateIterator<>(inside, isNeighbour);
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
            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(walls.get(MathUtils.random(walls.size - 1))))
                    .setRotation(Cell.ROTATE_270);
            dungeon.setCell(0, y, cell);
        }
        for (x = 1; x < dungeon.getWidth(); x++) {
            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(walls.get(MathUtils.random(walls.size - 1))));
            dungeon.setCell(x, 0, cell);
        }

        // Go through the maze and fill the map up with walls
        for (x = 0; x < mazeData.length; x++) {
            Field[] row = mazeData[x];
            for (y = 0; y < row.length; y++) {
                Field path = row[y];

                // Set texture on the path
                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(pathTexture));
                dungeon.setCell(x * 2 + 1, y * 2 + 1, cell);

                // Set a wall or path on the top
                Cell up = new Cell();
                up.setTile(new StaticTiledMapTile(
                        path.canUp() ? pathTexture : walls.get(MathUtils.random(walls.size - 1))));
                dungeon.setCell(x * 2 + 1, y * 2 + 2, up);

                // Set a wall or path on the right
                Cell right = new Cell();
                right.setTile(new StaticTiledMapTile(
                        path.canRight() ? pathTexture : walls.get(MathUtils.random(walls.size - 1))));
                // Randomize rotation on the right
                right.setRotation(MathUtils.randomBoolean() ? Cell.ROTATE_90 : Cell.ROTATE_270);
                dungeon.setCell(x * 2 + 2, y * 2 + 1, right);

                // Set the wall in the top-right corner to the path
                Cell upRight = new Cell();
                upRight.setTile(new StaticTiledMapTile(walls.get(MathUtils.random(walls.size - 1))));
                upRight.setRotation(MathUtils.randomBoolean() ? Cell.ROTATE_90 : Cell.ROTATE_270);
                dungeon.setCell(x * 2 + 2, y * 2 + 2, upRight);
            }
        }
    }

    private int enemyNum;
    private int shopNum;
    private int trapNum;

    private void generateInteractives() {
        // Use the maze's dimensions or one bigger number if they're odd
        float maxInteractives = (mazeWidth % 2 == 0 ? mazeWidth : mazeWidth + 1) * (mazeHeight % 2 == 0 ? mazeHeight : mazeHeight + 1);
        enemyNum = MathUtils.round(maxInteractives * enemiesPercent / 100);
        shopNum = MathUtils.round(maxInteractives * shopsPercent / 100);
        trapNum = MathUtils.round(maxInteractives * trapsPercent / 100);

        // Go through the maze by 2x2 square areas and place an interactive in every one of them
        for (int i = 0; i < mazeWidth; i += 2) {
            for (int j = 0; j < mazeHeight; j += 2) {
                placeInteractive(
                        i,
                        Math.min(i + 2, mazeWidth),
                        j,
                        Math.min(j + 2, mazeHeight)
                );
            }
        }
        // If any interactives left, place them somewhere
        while (enemyNum + shopNum + trapNum > 0) {
            placeInteractive(0, mazeWidth, 0, mazeHeight);
        }
    }

    /**
     * Place an interactive in the given area
     *
     * @param xMin The boundaries of the area
     * @param xMax The boundaries of the area
     * @param yMin The boundaries of the area
     * @param yMax The boundaries of the area
     */
    private void placeInteractive(int xMin, int xMax, int yMin, int yMax) {
        TiledMapTileLayer interactivesLayer = (TiledMapTileLayer) map.getLayers().get("interactives");
        gerda.arcfej.dreamrealm.map.interactives.Interactive interactive = null;
        // The coordinates where the interactive will be placed
        int x = 0;
        int y = 0;
        boolean allowed = false;

        int errorCount = 0;

        while (!allowed) {
            do {
                // Find a path which doesn't have an interactive yet
                x = MathUtils.random(xMax - xMin - 1) + xMin;
                y = MathUtils.random(yMax - yMin - 1) + yMin;
            } while (mazeData[x][y].getInteractive() != null);
            // Choose a random type on interactive.
            // TODO optimize this. How not choose a type which is not allowed any more? (no more left)
            switch (MathUtils.random(2)) {
                case 0:
                    interactive = enemyNum > 0 ? new gerda.arcfej.dreamrealm.map.interactives.Enemy() : null;
                    break;
                case 1:
                    interactive = shopNum > 0 ? new Shop() : null;
                    break;
                case 2:
                    interactive = trapNum > 0 ? new gerda.arcfej.dreamrealm.map.interactives.Trap() : null;
            }
            // Check if the interactive is allowed there
            allowed = isPlacementAllowed(interactive, x, y);
            errorCount++;
            if (errorCount > 10) {
                // If the placement was unsuccessful 10 times, don't place the interactive anywhere
                // TODO not ideal approach. Optimize placement randomization
                break;
            }
        }
        // Place the interactive if the randomization was successful
        if (interactive != null) {
            interactive.setPosition(x, y);
            mazeData[x][y].setInteractive(interactive);
            Cell cell = new Cell();
            // Reduce the corresponding interactive's counter and set the tile on the map
            if (interactive instanceof gerda.arcfej.dreamrealm.map.interactives.Enemy) {
                enemyNum--;
                cell.setTile(new StaticTiledMapTile(enemies.get(MathUtils.random(enemies.size - 1))));
            } else if (interactive instanceof Shop) {
                shopNum--;
                cell.setTile(new StaticTiledMapTile(shop));
            } else if (interactive instanceof Trap) {
                trapNum--;
                cell.setTile(new StaticTiledMapTile(trap));
            }
            interactivesLayer.setCell(x * 2 + 1, y * 2 + 1, cell);
        }
    }

    /**
     * Checks if the given interactive is allowed to be placed at the given coordinates
     */
    private boolean isPlacementAllowed(gerda.arcfej.dreamrealm.map.interactives.Interactive interactive, int x, int y) {
        if (interactive == null) {
            // Null placement is not allowed
            return false;
        }
        // Only 2 interactives is allowed in a 2x2 square. Count the existing ones.
        int count = 0;
        // Start from x-1 if it's inside the maze and go till... TODO check if this is correct
        for (int i = (x == 0 ? x : x - 1); i <= (x >= mazeWidth ? mazeWidth - 1 : x); i++) {
            // Start from y-1 if it's inside the maze and go till... TODO check if this is correct
            for (int j = (y == 0 ? y : y - 1); j <= (j >= mazeHeight ? mazeHeight - 1 : j); j++) {
                gerda.arcfej.dreamrealm.map.interactives.Interactive existing = mazeData[i][j].getInteractive();
                if (existing != null) {
                    count++;
                    if (interactive.getClass().equals(existing.getClass())) {
                        // Only one instance of an interactive is allowed in a 2x2 square
                        return false;
                    }
                }
            }
        }
        // Only 2 interactives is allowed in a 2x2 square. If there are more then one already, return false.
        return count < 2;
    }

    private void generateExit() {
        // Generate an exit at the farthest quarter from the player
        boolean top = player.y <= mazeWidth / 2;
        boolean left = player.x > mazeHeight / 2;
        boolean onHorizontal = MathUtils.randomBoolean();

        int x, y;
        // Exit on horizontal or vertical side
        if (onHorizontal) {
            // horizontal
            x = left ? MathUtils.random(0, mazeWidth / 2) : MathUtils.random(mazeWidth / 2, mazeWidth - 1);
            y = top ? 0 : mazeHeight - 1;
        } else {
            // vertical
            x = left ? 0 : mazeWidth - 1;
            y = top ? MathUtils.random(mazeHeight / 2, mazeHeight - 1) : MathUtils.random(0, mazeHeight / 2);
        }

        TiledMapTileLayer dungeon = (TiledMapTileLayer) map.getLayers().get("dungeon");
        Cell exit = new Cell();
        exit.setTile(new StaticTiledMapTile(this.exit));

        Field exitData = mazeData[x][y];
        if (onHorizontal) {
            if (top) {
                exitData.setUp(true);
                dungeon.setCell(x * 2 + 1, mapHeight - 1, exit);
            } else {
                exitData.setDown(true);
                exit.setFlipVertically(true);
                dungeon.setCell(x * 2 + 1, 0, exit);
            }
        } else {
            if (left) {
                exitData.setLeft(true);
                exit.setRotation(Cell.ROTATE_90);
                dungeon.setCell(0, y * 2 + 1, exit);
            } else {
                exitData.setRight(true);
                exit.setRotation(Cell.ROTATE_270);
                dungeon.setCell(mapWidth - 1, y * 2 + 1, exit);
            }
        }
    }

    // DISPLAYING

    /**
     * The bounds of the map on the screen in screen coordinates
     *
     * @param x The bottom-left corner of the bounds
     * @param y The bottom-left corner of the bounds
     */
    public void setScreenBounds(int x, int y, int width, int height) {
        getViewport().update(width, height);
        getViewport().setScreenBounds(x / 2, y / 2, width, height);
        ((OrthographicCamera) getCamera()).setToOrtho(false, mapWidth, mapHeight);
    }

    @Override
    public void draw() {
        // Set zoom based on view distance
        ((OrthographicCamera) getCamera()).zoom = (viewDistance * 4 + 3) / getCamera().viewportWidth;
        // Center the map on the player
//        camera.position.set(mapWidth / 2, mapHeight / 2, 0);
        getCamera().position.set(player.x * 2 + 1.5f,
                player.y * 2 + 1.5f,
                0);
        getCamera().update();

        // Draw
        getBatch().setProjectionMatrix(getCamera().combined);
        getViewport().apply();
        renderer.render();
        super.draw();
    }

    // MODIFICATIONS

    /**
     * Move the player in the given directions
     *
     * @param direction 0 - up
     *                  1 - right
     *                  2 - down
     *                  3 - left
     */
    public void movePlayer(int direction) {
        int currentX = player.x;
        int currentY = player.y;
        Field current = mazeData[currentX][currentY];
        int nextX = currentX;
        int nextY = currentY;

        // Check if the player can move in the direction or not
        switch (direction) {
            case 0:
                if (mazeData[currentX][currentY].canUp()) {
                    nextY++;
                    break;
                } else return;
            case 1:
                if (mazeData[currentX][currentY].canRight()) {
                    nextX++;
                    break;
                } else return;
            case 2:
                if (mazeData[currentX][currentY].canDown()) {
                    nextY--;
                    break;
                } else return;
            case 3:
                if (mazeData[currentX][currentY].canLeft()) {
                    nextX--;
                    break;
                } else return;
            default: return;
        }

        Field destination = null;
        // Check for winning
        try {
            destination = mazeData[nextX][nextY];
        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO win
            return;
        }

        player.setPosition(nextX, nextY);
        current.setHasPlayer(false);
        destination.setHasPlayer(true);
        TiledMapTileLayer layer = ((TiledMapTileLayer) map.getLayers().get("player"));
        layer.setCell(nextX * 2 + 1, nextY * 2 + 1, playerCell);
        layer.setCell(currentX * 2 + 1, currentY * 2 + 1, null);
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    // DESTRUCTION

    @Override
    public void dispose() {
        dungeon.dispose();
        interactives.dispose();
        enemyAtlas.dispose();
        map.dispose();
    }
}
