package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Rotates the entity continuously.
 */
@XmlRootElement
public final class SpinPart {
	
	@XmlElement
	private float rotationSpeed;
	
	public SpinPart() {
	}
	
	/**
	 * @return number of degrees to add to rotation per second
	 */
	public final float getRotationSpeed() {
		return rotationSpeed;
	}
	
}
