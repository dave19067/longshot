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
		float[] vertices = createRectangleVertices(size);
		return new Polygon(vertices);
	}
	
	public static final float[] createRectangleVertices(final Vector2 size) {
		return new float[] { 0, 0, size.x, 0, size.x, size.y, 0, size.y };
	}
	
}
