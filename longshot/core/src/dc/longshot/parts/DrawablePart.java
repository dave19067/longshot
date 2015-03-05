package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.badlogic.gdx.graphics.g2d.PolygonSprite;

@XmlRootElement
public final class DrawablePart {

	// TODO: separate this
	@XmlElement
	private String textureName;
	private PolygonSprite sprite;
	@XmlElement
	private float z;
	
	public DrawablePart() {
	}
	
	public DrawablePart(final PolygonSprite sprite, final float z) {
		this.sprite = sprite;
		this.z = z;
	}
	
	public final String getTextureName() {
		return textureName;
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
