package dc.longshot.geometry;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public final class PolygonUtils {
	
	private PolygonUtils() {
	}
	
	public static final void flipY(final float[] vertices) {
		float maxY = maxY(vertices);
		for (int i = 0; i < vertices.length / 2; i++) {
			vertices[i * 2 + 1] = maxY - vertices[i * 2 + 1];
		}
	}
	
	public static final float minX(final float[] vertices) {
		float minX = vertices[0];
		for (int i = 0; i < vertices.length / 2; i++) {
			if (vertices[i * 2] < minX) {
				minX = vertices[i * 2];
			}
		}
		return minX;
	}
	
	public static final float maxX(final float[] vertices) {
		float maxX = vertices[0];
		for (int i = 0; i < vertices.length / 2; i++) {
			if (vertices[i * 2] > maxX) {
				maxX = vertices[i * 2];
			}
		}
		return maxX;
	}
	
	public static final float minY(final float[] vertices) {
		float minY = vertices[1];
		for (int i = 0; i < vertices.length / 2; i++) {
			if (vertices[i * 2 + 1] < minY) {
				minY = vertices[i * 2 + 1];
			}
		}
		return minY;
	}
	
	public static final float maxY(final float[] vertices) {
		float maxY = vertices[1];
		for (int i = 0; i < vertices.length / 2; i++) {
			if (vertices[i * 2 + 1] > maxY) {
				maxY = vertices[i * 2 + 1];
			}
		}
		return maxY;
	}
	
	public static final Vector2 size(final Polygon polygon) {
		float rotation = polygon.getRotation();
		polygon.setRotation(0);
		Vector2 size = polygon.getBoundingRectangle().getSize(new Vector2());
		polygon.setRotation(rotation);
		return size;
	}
	
	public static final Vector2 size(final float[] vertices) {
		float width = maxX(vertices) - minX(vertices);
		float height = maxY(vertices) - minY(vertices);
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
