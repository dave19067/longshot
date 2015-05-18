package dc.longshot.parts;

import dc.longshot.util.FloatRange;

public final class GroundExploderPart {

	private final String entityType;
	private final float explodeRate;
	private final FloatRange diameterRange;
	
	public GroundExploderPart(final String entityType, final float explodeRate, final FloatRange diameterRange) {
		this.entityType = entityType;
		this.explodeRate = explodeRate;
		this.diameterRange = diameterRange;
	}
	
	public final String getEntityType() {
		return entityType;
	}
	
	public final float getExplodeRate() {
		return explodeRate;
	}
	
	public final float createRandomDiameter() {
		return diameterRange.random();
	}

}
