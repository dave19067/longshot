package dc.longshot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import dc.longshot.LongshotGame;

public class DesktopLauncher {
	
	public static void main (final String[] arg) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Longshot";
		new LwjglApplication(new LongshotGame(), configuration);
	}
	
}
