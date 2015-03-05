package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class WanderMovementPart {

	@XmlElement
	private float decisionRate;
	@XmlElement
	private float idleToMoveRatio;
	
	public WanderMovementPart() {
	}
	
	/**
	 * @return average number of seconds per decision
	 */
	public final float getDecisionRate() {
		return decisionRate;
	}
	
	/**
	 * @return how often to stay in place than move on a decision
	 */
	public final float getIdleToMoveRatio() {
		return idleToMoveRatio;
	}
	
}
