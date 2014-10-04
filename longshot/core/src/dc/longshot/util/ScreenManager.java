package dc.longshot.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class ScreenManager {
	
	private List<Screen> screens = new ArrayList<Screen>();
	private List<Screen> screensToAdd = new ArrayList<Screen>();
	private List<Screen> screensToRemove = new ArrayList<Screen>();
	
	public void add(Screen screen) {
		screensToAdd.add(screen);
	}
	
	public void remove(Screen screen) {
		screensToRemove.add(screen);
	}
	
	public void update() {
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
	
	public void render() {
		for (Screen screen : screens) {
			screen.render(Gdx.graphics.getDeltaTime());
		}
	}
	
	public void resize(int width, int height) {
		for (Screen screen : screens) {
			screen.resize(width, height);
		}
	}

	public void dispose() {
		for (Screen screen : screens) {
			screen.dispose();
		}
	}

}
