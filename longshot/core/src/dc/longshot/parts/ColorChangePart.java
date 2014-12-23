package dc.longshot.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;

import dc.longshot.epf.Part;

public final class ColorChangePart extends Part {

	private final Color startColor;
	private final Color endColor;
	private final float maxChangeTime;
	private float changeTime = 0;
	
	public ColorChangePart(final float maxChangeTime, final Color startColor, final Color endColor) {
		this.startColor = startColor;
		this.endColor = endColor;
		this.maxChangeTime = maxChangeTime;
	}

	@Override
	public final void update(final float delta) {
		changeTime += delta;
		float changePercent = changeTime / maxChangeTime;
		Color color = startColor.cpy().lerp(endColor.r, endColor.g, endColor.b, endColor.a, changePercent);
		PolygonSprite sprite = entity.get(DrawablePart.class).getSprite();
		sprite.setColor(color);
	}
	
}
