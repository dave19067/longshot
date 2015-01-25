package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.models.EntityType;
import dc.longshot.util.Timer;

public final class WeaponPart extends Part {
	
	private final EntityType entityType;
	private final List<Entity> spawns = new ArrayList<Entity>();
	private final int maxSpawns;
	private final Timer spawnTimer;

	public WeaponPart(final EntityType entityType, final int maxSpawns, final float maxSpawnTime) {
		this.entityType = entityType;
		this.maxSpawns = maxSpawns;
		spawnTimer = new Timer(maxSpawnTime);
	}
	
	public final EntityType getEntityType() {
		return entityType;
	}
	
	public final void addSpawn(final Entity spawn) {
		spawns.add(spawn);
	}
	
	public final boolean canSpawn() {
		return spawnTimer.isElapsed() && spawns.size() < maxSpawns;
	}
	
	public final void reset() {
		spawnTimer.reset();
	}
	
	public final void update(final float delta) {
		spawnTimer.tick(delta);
		Iterator<Entity> it = spawns.iterator();
		while (it.hasNext()) {
			Entity spawn = it.next();
			if (!spawn.isActive()) {
				it.remove();
			}
		}
	}
	
}
