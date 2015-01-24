package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.util.Cloning;
import dc.longshot.util.Timer;

public final class WeaponPart extends Part {
	
	private final Entity original;
	private final List<Entity> spawns = new ArrayList<Entity>();
	private final int maxSpawns;
	private final Timer spawnTimer;

	public WeaponPart(final Entity original, final int maxSpawns, final float maxSpawnTime) {
		this.original = original;
		this.maxSpawns = maxSpawns;
		spawnTimer = new Timer(maxSpawnTime);
	}
	
	public final boolean canSpawn() {
		return spawnTimer.isElapsed() && spawns.size() < maxSpawns;
	}
	
	public final Entity createSpawn() {
		if (canSpawn()) {
			spawnTimer.reset();
			Entity spawn = Cloning.clone(original);
			TransformPart spawnTransform = spawn.get(TransformPart.class);
			TransformPart transform = entity.get(TransformPart.class);
			spawnTransform.setPosition(PolygonUtils.relativeCenter(transform.getCenter(), 
					spawnTransform.getBoundingSize()));
			spawns.add(spawn);
			return spawn;
		}
		else {
			throw new IllegalStateException("Cannot create spawn");
		}
	}

	@Override
	public final void cleanup() {
		original.cleanup();
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
