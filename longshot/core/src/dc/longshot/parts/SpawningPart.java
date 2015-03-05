package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.models.SpawningType;

@XmlRootElement
public final class SpawningPart {

	@XmlElement
	private SpawningType spawningType;
	
	public SpawningPart() {
	}
	
	public SpawningType getSpawningType() {
		return spawningType;
	}
	
}
