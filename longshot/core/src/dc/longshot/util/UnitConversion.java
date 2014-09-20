package dc.longshot.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class UnitConversion {
	
	public static final float PIXELS_PER_UNIT = 64;

	public static Vector2 getScreenToWorldCoords(Camera camera, int screenX, int screenY) {
		Vector3 worldCoords3 = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoords3);
		worldCoords3.scl(1 / PIXELS_PER_UNIT);
		Vector2 worldCoords = new Vector2(worldCoords3.x, worldCoords3.y);
		return worldCoords;
	}
	
	public static Vector2 getScreenToWorldCoords(Camera camera, int screenX, int screenY, Rectangle viewPort) {
		Vector3 worldCoords3 = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoords3, viewPort.x, viewPort.y, viewPort.width, viewPort.height);
		worldCoords3.scl(1 / PIXELS_PER_UNIT);
		Vector2 worldCoords = new Vector2(worldCoords3.x, worldCoords3.y);
		return worldCoords;
	}
	
}
