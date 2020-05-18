package gerda.arcfej.dreamrealm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import gerda.arcfej.dreamrealm.GameCore;

public abstract class AbstractFixSizedScreen extends ScreenAdapter {

    /**
     * The controller of the game
     */
    protected GameCore gameCore;

    /**
     * The main input handler (as an input multiplexer)
     * Add as many sub-input handler to it as you'd like to
     */
    protected InputMultiplexer inputMultiplexer;

    /**
     * The stage to display the menu on.
     */
    protected Stage stage;

    public AbstractFixSizedScreen(GameCore gameCore, SpriteBatch batch) {
        this.gameCore = gameCore;
        stage = new Stage(new ScreenViewport(), batch);
        inputMultiplexer = new InputMultiplexer(stage);

        // Set rendering not continuous (save battery), because the game is static unless user input happens.
        Gdx.graphics.setContinuousRendering(false);
        Gdx.graphics.requestRendering();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
