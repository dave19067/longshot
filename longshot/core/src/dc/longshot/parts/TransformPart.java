package dc.longshot.parts;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.util.VectorUtils;

public class TransformPart extends Part {

	private Vector2 size;
	private Vector2 position;
	
	public TransformPart(Vector2 size) {
		this(size, new Vector2(0, 0));
	}
	
	public TransformPart(Vector2 size, Vector2 position) {
		this.size = size;
		this.position = position;
	}
	
	public Vector2 getSize() {
		return new Vector2(size);
	}
	
	public Vector2 getPosition() {
		return new Vector2(position);
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public Vector2 getCenter() {
		return VectorUtils.center(position, size);
	}
	
	public Rectangle getBoundingBox() {
		Vector2 position = getPosition();
		Vector2 size = getSize();
		return new Rectangle(position.x, position.y, size.x, size.y);
	}
	
}
