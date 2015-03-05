package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.models.Alliance;

@XmlRootElement
public final class AlliancePart {

	@XmlElement
	private Alliance alliance;
	
	public AlliancePart() {
	}
	
	public final Alliance getAlliance() {
		return alliance;
	}
	
}
