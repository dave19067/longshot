package dc.longshot.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import dc.longshot.ui.controls.EscapeMenu;

public final class GameInputProcessor implements InputProcessor {
	
	private final EscapeMenu escapeMenu;
	
	public GameInputProcessor(final EscapeMenu escapeMenu) {
		this.escapeMenu = escapeMenu;
	}
	
	@Override
	public final boolean keyDown(final int keycode) {
		return false;
	}

	@Override
	public final boolean keyUp(final int keycode) {
		switch (keycode) {
		case Keys.ESCAPE:
			escapeMenu.showDialog();
			return true;
		};
		return false;
	}

	@Override
	public final boolean keyTyped(final char character) {
		return false;
	}

	@Override
	public final boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		return false;
	}

	@Override
	public final boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		return false;
	}

	@Override
	public final boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		return false;
	}

	@Override
	public final boolean mouseMoved(final int screenX, final int screenY) {
		return false;
	}

	@Override
	public final boolean scrolled(final int amount) {
		return false;
	}

}
