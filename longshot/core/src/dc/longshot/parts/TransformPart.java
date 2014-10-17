package dc.longshot.parts;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonFactory;

public final class TransformPart extends Part {

	private final Polygon polygon;
	
	public TransformPart(final Vector2 size) {
		this(size, new Vector2());
	}
	
	public TransformPart(final Vector2 size, final Vector2 position) {
		polygon = PolygonFactory.createRectangle(size);
		polygon.setPosition(position.x, position.y);
		polygon.setOrigin(0, 0);
	}
	
	public final List<Vector2> getTransformedVertices() {
		List<Vector2> verticesList = new ArrayList<Vector2>();
		float[] vertices = polygon.getTransformedVertices();
		for (int i = 0; i < vertices.length / 2; i++) {
			verticesList.add(new Vector2(vertices[i * 2], vertices[i * 2 + 1]));
		}
		return verticesList;
	}
	
	public final Vector2 getSize() {
		float rotation = polygon.getRotation();
		polygon.setRotation(0);
		Vector2 size = polygon.getBoundingRectangle().getSize(new Vector2());
		polygon.setRotation(rotation);
		return size;
	}
	
	public final Vector2 getBoundingSize() {
		return polygon.getBoundingRectangle().getSize(new Vector2());
	}
	
	public final Vector2 getPosition() {
		return new Vector2(polygon.getX(), polygon.getY());
	}
	
	public final void setPosition(final Vector2 position) {
		polygon.setPosition(position.x, position.y);
	}
	
	public final Vector2 getOrigin() {
		return new Vector2(polygon.getOriginX(), polygon.getOriginY());
	}
	
	public final void setOrigin(final Vector2 origin) {
		polygon.setOrigin(origin.x, origin.y);
	}
	
	public final float getRotation() {
		return polygon.getRotation();
	}
	
	public final void setRotation(final float degrees) {
		polygon.setRotation(degrees);
	}
	
	public final Vector2 getCenter() {
		return polygon.getBoundingRectangle().getCenter(new Vector2());
	}
	
	public Rectangle getBoundingBox() {
		return polygon.getBoundingRectangle();
	}
	
}
