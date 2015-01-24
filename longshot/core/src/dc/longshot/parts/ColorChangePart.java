package dc.longshot.parts;

import com.badlogic.gdx.graphics.Color;

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
	
	public final Color getStartColor() {
		return startColor;
	}
	
	public final Color getEndColor() {
		return endColor;
	}
	
	public final Timer getChangeTimer() {
		return changeTimer;
	}
	
}
