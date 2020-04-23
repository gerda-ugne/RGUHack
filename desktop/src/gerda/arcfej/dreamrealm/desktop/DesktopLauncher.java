package gerda.arcfej.dreamrealm.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import gerda.arcfej.dreamrealm.GameCore;

public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Dream Realm");
		config.setWindowedMode(800, 480);

		new Lwjgl3Application(new GameCore(), config);
	}
}
