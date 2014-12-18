package dc.longshot.geometry;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class UnitConvert {
	
	public static final float PIXELS_PER_UNIT = 64;
	
	private UnitConvert() {
	}
	
	public static final Vector2 worldToPixel(final Vector2 worldCoords) {
		return worldCoords.cpy().scl(PIXELS_PER_UNIT);
	}
	
	public static final Vector2 pixelToWorld(final float screenX, final float screenY) {
		return new Vector2(screenX / PIXELS_PER_UNIT, screenY / PIXELS_PER_UNIT);
	}
	
	public static final Vector2 screenToWorld(final Camera camera, final float screenX, final float screenY) {
		Rectangle viewPort = new Rectangle(0, 0, camera.viewportWidth, camera.viewportHeight);
		return screenToWorld(camera, screenX, screenY, viewPort);
	}
	
	public static final Vector2 screenToWorld(final Camera camera, final float screenX, final float screenY, 
			final Rectangle viewPort) {
		Vector3 worldCoords3 = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoords3, viewPort.x, viewPort.y, viewPort.width, viewPort.height);
		worldCoords3.scl(1 / PIXELS_PER_UNIT);
		return new Vector2(worldCoords3.x, worldCoords3.y);
	}
	
}
