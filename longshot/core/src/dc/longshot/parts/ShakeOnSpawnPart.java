package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShakeOnSpawnPart {

	@XmlElement
	private float maxRadius;
	@XmlElement
	private float duration;
	
	public ShakeOnSpawnPart() {
	}
	
	public final float getMaxRadius() {
		return maxRadius;
	}
	
	public final float getDuration() {
		return duration;
	}

}
