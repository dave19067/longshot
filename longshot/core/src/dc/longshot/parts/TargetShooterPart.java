package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.models.Alliance;

@XmlRootElement
public final class TargetShooterPart {

	@XmlElement
	private float shootRate;
	@XmlElement
	private Alliance targetAlliance;
	
	public TargetShooterPart() {
	}
	
	public final float getShootRate() {
		return shootRate;
	}
	
	public final Alliance getTargetAlliance() {
		return targetAlliance;
	}

}
