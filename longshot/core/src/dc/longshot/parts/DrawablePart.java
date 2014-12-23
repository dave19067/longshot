package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.PolygonSprite;

import dc.longshot.epf.Part;

public final class DrawablePart extends Part {

	private final PolygonSprite sprite;
	private final float z;
	
	public DrawablePart(final PolygonSprite sprite, final float z) {
		this.sprite = sprite;
		this.z = z;
	}
	
	public final PolygonSprite getSprite() {
		return sprite;
	}
	
	public final float getZ() {
		return z;
	}
	
}
