package dc.longshot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

import dc.longshot.models.GameSettings;

public final class GameSettingsApplier {

	private GameSettingsApplier() {
	}
	
	public static final void apply(final GameSettings gameSettings) {
		// HACK: libgdx bug.  setDisplayMode with displaymode parameter only works for fullscreen
		// and setDisplayMode with three parameters only works for windowed
		if (gameSettings.isFullScreen()) {
			for (DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
				if (displayMode.bitsPerPixel == gameSettings.getBitsPerPixel()
						&& displayMode.height == gameSettings.getHeight()
						&& displayMode.width == gameSettings.getWidth()
						&& displayMode.refreshRate == gameSettings.getRefreshRate()) {
					Gdx.graphics.setDisplayMode(displayMode);
					break;
				}
			}
		}
		else {
			Gdx.graphics.setDisplayMode(gameSettings.getWidth(), gameSettings.getHeight(), gameSettings.isFullScreen());
		}
	}
	
}
