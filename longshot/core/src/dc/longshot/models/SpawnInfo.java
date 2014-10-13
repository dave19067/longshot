package dc.longshot.models;

public final class SpawnInfo {

	private final EntityType entityType;
	private final float spawnTime;
	
	public SpawnInfo(final EntityType entityType, final float spawnTime) {
		this.entityType = entityType;
		this.spawnTime = spawnTime;
	}
	
	public final EntityType getEntityType() {
		return entityType;
	}
	
	public final float getSpawnTime() {
		return spawnTime;
	}
	
}
