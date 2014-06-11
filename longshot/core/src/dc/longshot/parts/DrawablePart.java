package dc.longshot.parts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dc.longshot.epf.Part;

public class DrawablePart extends Part {

	private TextureRegion textureRegion;
	
	public DrawablePart(Texture texture) {
		this.textureRegion = new TextureRegion(texture);
	}
	
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
	
	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
}
