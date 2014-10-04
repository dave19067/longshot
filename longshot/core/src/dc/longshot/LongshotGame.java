package dc.longshot;

import com.badlogic.gdx.Game;

import dc.longshot.screens.MainMenuScreen;
import dc.longshot.ui.Skins;
import dc.longshot.util.ScreenManager;

public class LongshotGame extends Game {
	
	private ScreenManager screenManager = new ScreenManager();
	
	@Override
	public void create() {
		MainMenuScreen mainMenuScreen = new MainMenuScreen(screenManager);
		screenManager.add(mainMenuScreen);
	}

	@Override
	public void render() {
		screenManager.render();
		screenManager.update();
	}
	
	@Override
	public void resize(int width, int height) {
		screenManager.resize(width, height);
	}

	@Override
	public void dispose() {
		screenManager.dispose();
		Skins.dispose();
	}
	
}
