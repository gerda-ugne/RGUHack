package gerda.arcfej.dreamrealm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameCore extends ApplicationAdapter {

	/**
	 * Textures to be used in the game.
	 */
	private Texture[] walls;
	private Texture exit;
	private Texture path;
	private Texture player;
	private Texture[] enemies;
	private Texture deadEnemy;
	private Texture shop;
	private Texture trap;

	/**
	 * Used to show only a small size of a bigger canvas on the screen
	 */
	private OrthographicCamera camera;

	private final float viewPortWidth = 800;
	private final float viewportHeight = 480;

	/**
	 * Used for drawing 2d images to the screen
	 */
	private SpriteBatch batch;

	/**
	 * The map to display
	 */
	private Pixmap map;

	/**
	 * Width of the map
	 */
	private final int mapWidth = 450;

	/**
	 * Height of the map
	 */
	private final int mapHeight = 450;

	@Override
	public void create () {
		// Load pictures
		walls = new Texture[3];
		walls[0] = new Texture(Gdx.files.internal("dungeon/brick-wall.png"));
		walls[1] = new Texture(Gdx.files.internal("dungeon/broken-wall.png"));
		walls[2] = new Texture(Gdx.files.internal("dungeon/stone-wall.png"));

		exit = new Texture(Gdx.files.internal("dungeon/dungeon-gate.png"));
		path = new Texture(Gdx.files.internal("dungeon/path-tile.png"));
		player = new Texture(Gdx.files.internal("hooded-figure.png"));

		enemies = new Texture[14];
		enemies[0] = new Texture(Gdx.files.internal("enemies/bully-minion.png"));
		enemies[1] = new Texture(Gdx.files.internal("enemies/ceiling-barnacle.png"));
		enemies[2] = new Texture(Gdx.files.internal("enemies/evil-bat.png"));
		enemies[3] = new Texture(Gdx.files.internal("enemies/evil-minion.png"));
		enemies[4] = new Texture(Gdx.files.internal("enemies/gargoyle.png"));
		enemies[5] = new Texture(Gdx.files.internal("enemies/gooey-daemon.png"));
		enemies[6] = new Texture(Gdx.files.internal("enemies/grim-reaper.png"));
		enemies[7] = new Texture(Gdx.files.internal("enemies/ice-golem.png"));
		enemies[8] = new Texture(Gdx.files.internal("enemies/minotaur.png"));
		enemies[9] = new Texture(Gdx.files.internal("enemies/shambling-mound.png"));
		enemies[10] = new Texture(Gdx.files.internal("enemies/skeleton.png"));
		enemies[11] = new Texture(Gdx.files.internal("enemies/spectre.png"));
		enemies[12] = new Texture(Gdx.files.internal("enemies/troglodyte.png"));
		enemies[13] = new Texture(Gdx.files.internal("enemies/werewolf.png"));

		deadEnemy = new Texture(Gdx.files.internal("tombstone.png"));
		shop = new Texture(Gdx.files.internal("shop.png"));
		trap = new Texture(Gdx.files.internal("wolf-trap.png"));

		// Initialize map
		map = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGB888);

		// Initialize viewport
		camera = new OrthographicCamera();
		camera.setToOrtho(true, 800, 480);

		// Initialize batch (Which draws 2d images on the screen)
		batch = new SpriteBatch();

		// Set rendering not continuous (save battery), because the game is static unless user input happens.
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();
	}

	@Override
	public void render () {
		// Clear the screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Tell the camera to update its matrices
		camera.update();
		// Set the camera's matrix to the batch (to draw upon)
		batch.setProjectionMatrix(camera.combined);
		// Begin draw
		batch.begin();
			// Display the map as a blue rectangle
			map.setColor(0, 0, .4f, 1);
			map.fill();
			batch.draw(new Texture(map), viewPortWidth / 2 - mapWidth / 2f, viewportHeight / 2 - mapHeight / 2f);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

		for (Texture wall : walls) {
			wall.dispose();
		}
		exit.dispose();
		path.dispose();
		player.dispose();
		for (Texture enemy : enemies) {
			enemy.dispose();
		}
		deadEnemy.dispose();
		shop.dispose();
		trap.dispose();

		map.dispose();
	}
}
