package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;

import dc.longshot.epf.Part;

public final class DrawablePart extends Part {

	private final Sprite sprite;
	private final float z;
	
	public DrawablePart(final Sprite sprite, final float z) {
		this.sprite = sprite;
		this.z = z;
	}
	
	public final Sprite getSprite() {
		return sprite;
	}
	
	public final float getZ() {
		return z;
	}
	
}
