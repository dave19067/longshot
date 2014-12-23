package dc.longshot.geometry;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public final class PolygonFactory {
	
	private PolygonFactory() {
	}

	public static final Polygon copy(final Polygon polygon) {
		Polygon copy = new Polygon(polygon.getVertices());
		copy.setOrigin(polygon.getOriginX(), polygon.getOriginY());
		copy.setPosition(polygon.getX(), polygon.getY());
		copy.setRotation(polygon.getRotation());
		copy.setScale(polygon.getScaleX(), polygon.getScaleY());
		return copy;
	}
	
	public static final Polygon createRectangle(final Vector2 size) {
		float[] vertices = createRectangleVertices(size.x, size.y);
		return new Polygon(vertices);
	}
	
	public static final float[] createRectangleVertices(final float width, final float height) {
		return createRectangleVertices(0, 0, width, height);
	}
	
	public static final float[] createRectangleVertices(final float x, final float y, final float width, 
			final float height) {
		return new float[] { x, y, x + width, y, x + width, y + height, x, y + height };
	}
	
}
