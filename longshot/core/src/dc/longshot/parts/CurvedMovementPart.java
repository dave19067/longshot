package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class CurvedMovementPart {
	
	@XmlElement
	private float curveSize;
	
	public CurvedMovementPart() {
	}
	
	public final float getCurveSize() {
		return curveSize;
	}
	
}
