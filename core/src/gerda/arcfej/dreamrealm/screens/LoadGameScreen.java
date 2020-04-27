package gerda.arcfej.dreamrealm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import gerda.arcfej.dreamrealm.GameCore;

public class LoadGameScreen extends AbstractFullScreen {

    public LoadGameScreen(GameCore gameCore, SpriteBatch batch) {
        super(gameCore, batch);
        stage.addActor(new Label("Load Saved Games Screen", gameCore.skin));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // Clear the screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
