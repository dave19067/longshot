package dc.longshot.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import dc.longshot.epf.Part;

public class ColorChangePart extends Part {

	private Color startColor;
	private Color endColor;
	private float maxChangeTime;
	private float changeTime = 0;
	
	public ColorChangePart(float maxChangeTime, Color startColor, Color endColor) {
		this.startColor = startColor;
		this.endColor = endColor;
		this.maxChangeTime = maxChangeTime;
	}

	@Override
	public void update(float delta) {
		changeTime += delta;
		float changePercent = changeTime / maxChangeTime;
		Color color = startColor.cpy().lerp(endColor.r, endColor.g, endColor.b, endColor.a, changePercent);
		Sprite sprite = entity.get(DrawablePart.class).getSprite();
		sprite.setColor(color);
	}
	
}
