package dc.longshot.geometry;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class UnitConversion {
	
	public static final float PIXELS_PER_UNIT = 64;
	
	private UnitConversion() {
	}
	
	public static final Vector2 worldToScreen(final Vector2 worldCoords) {
		// TODO: May need to take into account the camera?
		return worldCoords.cpy().scl(PIXELS_PER_UNIT);
	}
	
	public static final Vector2 screenToWorld(final Camera camera, final int screenX, final int screenY) {
		Rectangle viewPort = new Rectangle(0, 0, camera.viewportWidth, camera.viewportHeight);
		Vector2 worldCoords = screenToWorld(camera, screenX, screenY, viewPort);
		return worldCoords;
	}
	
	public static final Vector2 screenToWorld(final Camera camera, final int screenX, final int screenY, 
			final Rectangle viewPort) {
		Vector3 worldCoords3 = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoords3, viewPort.x, viewPort.y, viewPort.width, viewPort.height);
		worldCoords3.scl(1 / PIXELS_PER_UNIT);
		Vector2 worldCoords = new Vector2(worldCoords3.x, worldCoords3.y);
		return worldCoords;
	}
	
}
