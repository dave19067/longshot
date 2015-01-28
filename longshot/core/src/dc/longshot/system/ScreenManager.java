package dc.longshot.system;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public final class ScreenManager {
	
	private final List<Screen> screens = new ArrayList<Screen>();
	private final List<Screen> screensToAdd = new ArrayList<Screen>();
	private final List<Screen> screensToRemove = new ArrayList<Screen>();
	
	public final Screen getCurrentScreen() {
		if (screens.isEmpty()) {
			throw new UnsupportedOperationException("Could not get current screen because there are no screens");
		}
		return screens.get(0);
	}
	
	public final void add(final Screen screen) {
		screensToAdd.add(screen);
		screen.show();
	}
	
	public final void remove(final Screen screen) {
		screensToRemove.add(screen);
		screen.hide();
	}
	
	public final void swap(final Screen currentScreen, final Screen newScreen) {
		remove(currentScreen);
		add(newScreen);
	}
	
	public final void update() {
		while (!screensToAdd.isEmpty()) {
			Screen screen = screensToAdd.remove(0);
			screens.add(screen);
		}
		
		while (!screensToRemove.isEmpty()) {
			Screen screen = screensToRemove.remove(0);
			screen.dispose();
			screens.remove(screen);
		}
	}
	
	public final void render() {
		for (Screen screen : screens) {
			screen.render(Gdx.graphics.getDeltaTime());
		}
	}
	
	public final void resize(final int width, final int height) {
		for (Screen screen : screens) {
			screen.resize(width, height);
		}
	}

	public final void dispose() {
		for (Screen screen : screens) {
			screen.dispose();
		}
	}

}
