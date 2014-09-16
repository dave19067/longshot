package dc.longshot.parts;

import com.badlogic.gdx.graphics.Color;

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
		startingAlpha = entity.get(DrawablePart.class).getColor().a;
	}

	@Override
	public void update(float delta) {
		fadeTime += delta;
		float fadePercent = fadeTime / maxFadeTime;
		float alpha = Math.max((1 - fadePercent) * startingAlpha, 0);
		DrawablePart drawablePart = entity.get(DrawablePart.class);
		Color color = drawablePart.getColor();
		color.a = alpha;
	}
	
}
