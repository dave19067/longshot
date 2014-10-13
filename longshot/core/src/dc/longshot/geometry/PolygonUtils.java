package dc.longshot.geometry;

import com.badlogic.gdx.math.Rectangle;

public final class PolygonUtils {

	public static final float top(final Rectangle rectangle) {
		return rectangle.y + rectangle.height;
	}
	
	public static final float right(final Rectangle rectangle) {
		return rectangle.x + rectangle.width;
	}
	
}
