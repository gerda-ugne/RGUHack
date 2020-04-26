package gerda.arcfej.dreamrealm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gerda.arcfej.dreamrealm.screens.GameScreen;
import gerda.arcfej.dreamrealm.screens.MenuScreen;

public class GameCore extends Game {

	/**
	 * The skin to use in the game
	 */
	public Skin skin;

	/**
	 * The main menu screen of the game
	 */
	private Screen mainMenu;

	/**
	 * The game screen
	 */
	private Screen game;

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
		mainMenu = new MenuScreen(this);
		game = new GameScreen(this);
		showMainMenu();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		mainMenu.dispose();
		game.dispose();
		skin.dispose();
	}

	public void showMainMenu() {
		setScreen(mainMenu);
	}

	public void startNewGame() {
		setScreen(game);
	}
}
