package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class WanderMovementPart extends Part {

	private final float decisionRate;
	private final float idleToMoveRatio;
	
	/**
	 * Constructor.
	 * @param decisionRate average number of seconds per decision
	 * @param idleToMoveRatio how often to stay in place than move on a decision
	 */
	public WanderMovementPart(final float decisionRate, final float idleToMoveRatio) {
		this.decisionRate = decisionRate;
		this.idleToMoveRatio = idleToMoveRatio;
	}
	
	public final float getDecisionRate() {
		return decisionRate;
	}
	
	public final float getIdleToMoveRatio() {
		return idleToMoveRatio;
	}
	
}
