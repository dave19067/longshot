package dc.longshot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import dc.longshot.LongshotGame;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "Longshot";
		configuration.fullscreen = false;
		configuration.width = 1024;
		configuration.height = 768;
		
		new LwjglApplication(new LongshotGame(), configuration);
	}
	
}
