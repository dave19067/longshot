package dc.longshot.geometry;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public final class PolygonUtils {
	
	private PolygonUtils() {
	}
	
	public static final Vector2 size(final Polygon polygon) {
		float rotation = polygon.getRotation();
		polygon.setRotation(0);
		Vector2 size = polygon.getBoundingRectangle().getSize(new Vector2());
		polygon.setRotation(rotation);
		return size;
	}
	
	public static final Vector2 size(final float[] vertices) {
		float width = vertices[0];
		float height = vertices[1];
		for (int i = 0; i < vertices.length; i++) {
			if (i % 2 == 0) {
				if (vertices[i] > width) {
					width = vertices[i];
				}
			}
			else {
				if (vertices[i] > height) {
					height = vertices[i];
				}
			}
		}
		return new Vector2(width, height);
	}

	public static final float top(final Rectangle rectangle) {
		return rectangle.y + rectangle.height;
	}
	
	public static final float right(final Rectangle rectangle) {
		return rectangle.x + rectangle.width;
	}
	
	public static final void translateY(final Rectangle rectangle, final float offsetY) {
		rectangle.setY(rectangle.y + offsetY);
		rectangle.setHeight(rectangle.height - offsetY);
	}
	
	public static final Vector2 center(final Polygon polygon) {
		return polygon.getBoundingRectangle().getCenter(new Vector2());
	}

	public static final Vector2 relativeCenter(final Vector2 pivot, final Vector2 objectSize) {
		Vector2 halfObjectSize = objectSize.cpy().scl(0.5f);
		return pivot.cpy().sub(halfObjectSize);
	}
	
	/**
	 * Convert a point local to the polygon to a point in global space.
	 * @param local local position within the untransformed polyon
	 * @param polygon polygon
	 * @return global point
	 */
	public static Vector2 toGlobal(final Vector2 local, final Polygon polygon) {
		return toGlobal(local.x, local.y, polygon);
	}
	
	/**
	 * Convert a point local to the polygon to a point in global space.
	 * @param localX local X within the untransformed polygon
	 * @param localY local Y within the untransformed polygon
	 * @param polygon polygon
	 * @return global point
	 */
	public static Vector2 toGlobal(final float localX, final float localY, final Polygon polygon) {
		return new Vector2(localX, localY)
			.sub(polygon.getOriginX(), polygon.getOriginY())
			.scl(polygon.getScaleX(), polygon.getScaleY())
			.rotate(polygon.getRotation())
			.add(polygon.getX(), polygon.getY())
			.add(polygon.getOriginX(), polygon.getOriginY());
	}
	
}
