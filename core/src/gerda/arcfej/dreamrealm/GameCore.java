package gerda.arcfej.dreamrealm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gerda.arcfej.dreamrealm.screens.GameScreen;
import gerda.arcfej.dreamrealm.screens.HowToPlayScreen;
import gerda.arcfej.dreamrealm.screens.LoadGameScreen;
import gerda.arcfej.dreamrealm.screens.MenuScreen;

public class GameCore extends Game {

	/**
	 * The skin to use in the game
	 */
	public Skin skin;

	/**
	 * Used to draw sprites on the screen
	 */
	private SpriteBatch batch;

	/**
	 * The main menu screen of the game
	 */
	private Screen mainMenu;

	/**
	 * The game screen
	 */
	private Screen game;

	/**
	 * The load saved game screen
	 */
	private Screen loadGame;

	/**
	 * Game guide screen
	 */
	private Screen howToPlay;

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
		batch = new SpriteBatch();

		mainMenu = new MenuScreen(this, batch);
		game = new GameScreen(this, batch);
		loadGame = new LoadGameScreen(this, batch);
		howToPlay = new HowToPlayScreen(this, batch);

// TODO		showMainMenu();
		startGame();
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		// Dispose screens
		mainMenu.dispose();
		game.dispose();
		loadGame.dispose();

		skin.dispose();
		batch.dispose();
	}

	// TODO create GameFunctions interface for this methods
	public void showMainMenu() {
		setScreen(mainMenu);
	}

	public void startGame() {
		setScreen(game);
	}

	public void loadGame() {
		setScreen(loadGame);
	}

	public void showHowToPlay() {
		setScreen(howToPlay);
	}
	// TODO end of GameFunctions interface methods
}
