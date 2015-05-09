package dc.longshot.parts;

public final class GroundExploderPart {

	private final String entityType;
	private final float explodeRate;
	private final float minSize;
	private final float maxSize;
	
	public GroundExploderPart(final String entityType, final float explodeRate, final float minSize, 
			final float maxSize) {
		this.entityType = entityType;
		this.explodeRate = explodeRate;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}
	
	public final String getEntityType() {
		return entityType;
	}
	
	public final float getExplodeRate() {
		return explodeRate;
	}
	
	public final float getMinSize() {
		return minSize;
	}
	
	public final float getMaxSize() {
		return maxSize;
	}

}
