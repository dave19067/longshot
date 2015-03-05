package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class SpawnOnDeathPart {

	@XmlElement
	private String entityType;
	
	public SpawnOnDeathPart() {
	}
	
	public final String getEntityType() {
		return entityType;
	}
	
}
