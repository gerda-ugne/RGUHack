package gerda.arcfej.dreamrealm.map;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;

public class PlayerController extends GestureAdapter implements InputProcessor {

    private Map map;

    public PlayerController(Map map) {
        this.map = map;
    }

    // GESTURE LISTENER METHODS

    /**
     * The gesture detector's touchDown event
     */
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return super.touchDown(x, y, pointer, button);
    }

    // INPUT PROCESSOR METHODS

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * The input processor's touchDown event
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
