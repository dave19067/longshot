package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;

import dc.longshot.epf.Part;

public class DrawablePart extends Part {

	private Sprite sprite;
	private float z;
	
	public DrawablePart(Sprite sprite, float z) {
		this.sprite = sprite;
		this.z = z;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public float getZ() {
		return z;
	}
	
}
