package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.PolygonSprite;

public final class DrawablePart {

	private PolygonSprite sprite;
	private float z;
	
	public DrawablePart() {
	}
	
	public DrawablePart(final PolygonSprite sprite, final float z) {
		this.sprite = sprite;
		this.z = z;
	}
	
	public final PolygonSprite getSprite() {
		return sprite;
	}
	
	public final void setSprite(final PolygonSprite sprite) {
		this.sprite = sprite;
	}
	
	public final float getZ() {
		return z;
	}
	
}
