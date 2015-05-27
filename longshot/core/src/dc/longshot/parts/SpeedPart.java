package dc.longshot.parts;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class SpeedPart {

	private float speed;
	
	public SpeedPart() {
	}
	
	public SpeedPart(final float speed) {
		this.speed = speed;
	}
	
	public final float getSpeed() {
		return speed;
	}
	
	public final void setSpeed(final float speed) {
		this.speed = speed;
	}
	
}
