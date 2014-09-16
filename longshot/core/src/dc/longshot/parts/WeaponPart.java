package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.util.Cloning;

public class WeaponPart extends Part {
	
	private Entity original;
	private List<Entity> spawns = new ArrayList<Entity>();
	private int maxSpawns;
	private float maxSpawnTime;
	private float spawnTime;

	public WeaponPart(Entity original, int maxSpawns, float maxSpawnTime) {
		this.original = original;
		this.maxSpawns = maxSpawns;
		this.maxSpawnTime = maxSpawnTime;
		this.spawnTime = maxSpawnTime;
	}
	
	public boolean canSpawn() {
		return spawnTime >= maxSpawnTime && spawns.size() < maxSpawns;
	}
	
	public Entity createSpawn() {
		if (canSpawn()) {
			spawnTime = 0;
			Entity newSpawn = Cloning.clone(original);
			spawns.add(newSpawn);
			return newSpawn;
		}
		else {
			throw new IllegalStateException("Cannot create spawn");
		}
	}
	
	@Override
	public void update(float delta) {
		spawnTime += delta;
		Iterator<Entity> it = spawns.iterator();
		while (it.hasNext()) {
			Entity spawn = it.next();
			if (!spawn.isActive()) {
				it.remove();
				break;
			}
		}
	}
	
}
