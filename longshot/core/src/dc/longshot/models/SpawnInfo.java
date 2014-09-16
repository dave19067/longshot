package dc.longshot.models;

public class SpawnInfo {

	private EntityType entityType;
	private float spawnTime;
	
	public SpawnInfo(EntityType entityType, float spawnTime) {
		this.entityType = entityType;
		this.spawnTime = spawnTime;
	}
	
	public EntityType getEntityType() {
		return entityType;
	}
	
	public float getSpawnTime() {
		return spawnTime;
	}
	
}
