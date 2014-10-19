package dc.longshot.geometry;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public final class PolygonUtils {

	public static final float top(final Rectangle rectangle) {
		return rectangle.y + rectangle.height;
	}
	
	public static final float right(final Rectangle rectangle) {
		return rectangle.x + rectangle.width;
	}

	public static final Vector2 relativeCenter(final Vector2 pivotCenter, final Vector2 objectSize) {
		Vector2 halfObjectSize = objectSize.cpy().scl(0.5f);
		return pivotCenter.cpy().sub(halfObjectSize);
	}
	
}
