package dc.longshot.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import dc.longshot.epf.Part;

public class FaderPart extends Part {

	private float startingAlpha;
	private float maxFadeTime;
	private float fadeTime = 0;
	
	public FaderPart(float maxFadeTime) {
		this.maxFadeTime = maxFadeTime;
	}
	
	@Override
	public void initialize() {
		startingAlpha = entity.get(DrawablePart.class).getSprite().getColor().a;
	}

	@Override
	public void update(float delta) {
		fadeTime += delta;
		float fadePercent = fadeTime / maxFadeTime;
		float alpha = Math.max((1 - fadePercent) * startingAlpha, 0);
		Sprite sprite = entity.get(DrawablePart.class).getSprite();
		Color color = sprite.getColor();
		color.a = alpha;
		sprite.setColor(color);
	}
	
}
