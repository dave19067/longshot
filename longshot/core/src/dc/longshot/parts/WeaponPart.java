package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.util.Cloning;

public final class WeaponPart extends Part {
	
	private final Entity original;
	private final List<Entity> spawns = new ArrayList<Entity>();
	private final int maxSpawns;
	private final float maxSpawnTime;
	private float spawnTime;

	public WeaponPart(final Entity original, final int maxSpawns, final float maxSpawnTime) {
		this.original = original;
		this.maxSpawns = maxSpawns;
		this.maxSpawnTime = maxSpawnTime;
		this.spawnTime = maxSpawnTime;
	}
	
	public final boolean canSpawn() {
		return spawnTime >= maxSpawnTime && spawns.size() < maxSpawns;
	}
	
	public final Entity createSpawn() {
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
	public final void cleanup() {
		original.cleanup();
	}
	
	@Override
	public final void update(final float delta) {
		spawnTime += delta;
		Iterator<Entity> it = spawns.iterator();
		while (it.hasNext()) {
			Entity spawn = it.next();
			if (!spawn.isActive()) {
				it.remove();
			}
		}
	}
	
}
