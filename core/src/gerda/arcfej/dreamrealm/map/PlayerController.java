package gerda.arcfej.dreamrealm.map;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;

import static com.badlogic.gdx.Input.*;

public class PlayerController extends GestureAdapter implements InputProcessor {

    private Map map;

    public PlayerController(Map map) {
        this.map = map;
    }

    // GESTURE LISTENER METHODS

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        // Move the player with gestures
        boolean isHorizontal = Math.abs(velocityX) > Math.abs(velocityY);
        if (isHorizontal) {
            if (velocityX > 0) map.movePlayer(1); // Move right
            else map.movePlayer(3); // Move left
        } else {
            if (velocityY > 0) map.movePlayer(0); // Move up
            else map.movePlayer(2); // Move down
        }
        return true;
    }

    // INPUT PROCESSOR METHODS

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.UP:
                map.movePlayer(0);
                break;
            case Keys.D:
            case Keys.RIGHT:
                map.movePlayer(1);
                break;
            case Keys.S:
            case Keys.DOWN:
                map.movePlayer(2);
                break;
            case Keys.A:
            case Keys.LEFT:
                map.movePlayer(3);
                break;
            default:
                return false;
        }

        return true;
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
