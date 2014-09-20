package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;

import dc.longshot.epf.Part;

public class DrawablePart extends Part {

	private Sprite sprite;
	
	public DrawablePart(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
}
