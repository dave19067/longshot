package dc.longshot;

import com.badlogic.gdx.Game;

import dc.longshot.game.Skins;
import dc.longshot.screens.MainMenuScreen;
import dc.longshot.system.ScreenManager;

public final class LongshotGame extends Game {
	
	private final ScreenManager screenManager = new ScreenManager();
	
	@Override
	public final void create() {
		MainMenuScreen mainMenuScreen = new MainMenuScreen(screenManager);
		screenManager.add(mainMenuScreen);
	}

	@Override
	public final void render() {
		screenManager.render();
		screenManager.update();
	}
	
	@Override
	public final void resize(final int width, final int height) {
		screenManager.resize(width, height);
	}

	@Override
	public final void dispose() {
		screenManager.dispose();
		Skins.dispose();
	}
	
}
