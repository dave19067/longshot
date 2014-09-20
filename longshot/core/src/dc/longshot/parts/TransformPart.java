package dc.longshot.parts;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.util.PolygonFactory;

public class TransformPart extends Part {

	private Polygon polygon;
	
	public TransformPart(Vector2 size) {
		this(size, new Vector2());
	}
	
	public TransformPart(Vector2 size, Vector2 position) {
		this(size, position, new Vector2(0, 0), 0);
	}
	
	public TransformPart(Vector2 size, Vector2 position, Vector2 origin, float rotation) {
		polygon = PolygonFactory.createRectangle(size);
		polygon.setPosition(position.x, position.y);
		polygon.setOrigin(origin.x, origin.y);
		polygon.setRotation(rotation);
	}
	
	public Vector2 getSize() {
		float rotation = polygon.getRotation();
		polygon.setRotation(0);
		Vector2 size = polygon.getBoundingRectangle().getSize(new Vector2());
		polygon.setRotation(rotation);
		return size;
	}
	
	public Vector2 getBoundedSize() {
		Vector2 size = polygon.getBoundingRectangle().getSize(new Vector2());
		return size;
	}
	
	public Vector2 getPosition() {
		return new Vector2(polygon.getX(), polygon.getY());
	}
	
	public void setPosition(Vector2 position) {
		polygon.setPosition(position.x, position.y);
	}
	
	public Vector2 getOrigin() {
		return new Vector2(polygon.getOriginX(), polygon.getOriginY());
	}
	
	public void setOrigin(Vector2 origin) {
		polygon.setOrigin(origin.x, origin.y);
	}
	
	public float getRotation() {
		return polygon.getRotation();
	}
	
	public void setRotation(float degrees) {
		polygon.setRotation(degrees);
	}
	
	public Vector2 getCenter() {
		return polygon.getBoundingRectangle().getCenter(new Vector2());
	}
	
	public Rectangle getBoundingBox() {
		return polygon.getBoundingRectangle();
	}
	
}
