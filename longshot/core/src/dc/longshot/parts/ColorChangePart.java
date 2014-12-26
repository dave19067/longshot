package dc.longshot.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;

import dc.longshot.epf.Part;
import dc.longshot.util.Timer;

public final class ColorChangePart extends Part {

	private final Color startColor;
	private final Color endColor;
	private final Timer changeTimer;
	
	public ColorChangePart(final float maxChangeTime, final Color startColor, final Color endColor) {
		this.startColor = startColor;
		this.endColor = endColor;
		changeTimer = new Timer(maxChangeTime);
	}

	@Override
	public final void update(final float delta) {
		changeTimer.tick(delta);
		Color color = startColor.cpy().lerp(endColor.r, endColor.g, endColor.b, endColor.a, 
				changeTimer.getElapsedPercent());
		PolygonSprite sprite = entity.get(DrawablePart.class).getSprite();
		sprite.setColor(color);
	}
	
}
