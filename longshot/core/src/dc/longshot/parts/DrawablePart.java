package dc.longshot.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dc.longshot.epf.Part;

public class DrawablePart extends Part {

	private TextureRegion textureRegion;
	private Color color = Color.WHITE;
	
	public DrawablePart(Texture texture) {
		this.textureRegion = new TextureRegion(texture);
	}
	
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
	
	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
}
