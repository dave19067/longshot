package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.graphics.Color;

import dc.longshot.util.Timer;
import dc.longshot.xmladapters.ColorAdapter;

@XmlRootElement
public final class ColorChangePart {

	@XmlJavaTypeAdapter(ColorAdapter.class)
	private Color startColor;
	@XmlJavaTypeAdapter(ColorAdapter.class)
	private Color endColor;
	@XmlElement
	private Timer changeTimer;
	
	public ColorChangePart() {
	}
	
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
