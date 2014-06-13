package dc.longshot.parts;

import com.rits.cloning.Cloner;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public class SpawnerPart extends Part {
	
	private Entity spawn;
	private float maxSpawnTime;
	private float spawnTime;

	public SpawnerPart(Entity spawn, float maxSpawnTime) {
		this.spawn = spawn;
		this.maxSpawnTime = maxSpawnTime;
		this.spawnTime = maxSpawnTime;
	}
	
	public boolean canSpawn() {
		return spawnTime >= maxSpawnTime;
	}
	
	public Entity createSpawn() {
		if (canSpawn()) {
			spawnTime = 0;
			Cloner cloner = new Cloner();
			Entity newSpawn = cloner.deepClone(spawn);
			return newSpawn;
		}
		else {
			throw new IllegalStateException("Cannot create spawn");
		}
	}
	
	@Override
	public void update(float delta) {
		spawnTime += delta;
	}
	
}
