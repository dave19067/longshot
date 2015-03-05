package dc.longshot.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class SpeedPart {

	@XmlElement
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
