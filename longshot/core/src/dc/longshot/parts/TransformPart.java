package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public class TransformPart extends Part {

	private Vector2 position;
	
	public TransformPart(Vector2 position) {
		this.position = position;
	}
	
	public Vector2 getPosition() {
		return new Vector2(position);
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
}
