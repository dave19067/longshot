package dc.longshot.models;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.badlogic.gdx.Graphics.DisplayMode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class GameSettings {

	private int width;
	private int height;
	private int refreshRate;
	private int bitsPerPixel;
	private boolean isFullScreen;
	@XmlElementWrapper
	private Map<InputAction, Integer> inputActions;
	
	public final int getWidth() {
		return width;
	}
	
	public final int getHeight() {
		return height;
	}
	
	public final int getRefreshRate() {
		return refreshRate;
	}
	
	public final int getBitsPerPixel() {
		return bitsPerPixel;
	}
	
	public final void setDisplayMode(final DisplayMode displayMode) {
		bitsPerPixel = displayMode.bitsPerPixel;
		height = displayMode.height;
		refreshRate = displayMode.refreshRate;
		width = displayMode.width;
	}
	
	public final boolean isFullScreen() {
		return isFullScreen;
	}
	
	public final void setFullScreen(final boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}
	
	public final Map<InputAction, Integer> getInputActions() {
		return new HashMap<InputAction, Integer>(inputActions);
	}
	
	public final void set(final InputAction inputAction, final int keycode) {
		inputActions.put(inputAction, keycode);
	}
	
}
