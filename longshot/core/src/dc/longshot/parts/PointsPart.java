package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class PointsPart {

	@XmlElement
	private int points;
	
	public PointsPart() {
	}
	
	public final int getPoints() {
		return points;
	}
	
}
