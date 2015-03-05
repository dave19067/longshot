package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityCache;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.parts.EmitPart;
import dc.longshot.parts.TransformPart;

public final class EmitSystem extends EntitySystem {

	private final EntityCache entityCache;
	private final EntityManager entityManager;
	
	public EmitSystem(final EntityCache entityCache, final EntityManager entityManager) {
		this.entityCache = entityCache;
		this.entityManager = entityManager;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(EmitPart.class)) {
			EmitPart emitPart = entity.get(EmitPart.class);
			emitPart.update(delta);
			if (emitPart.canEmit()) {
				Entity spawn = emit(entity);
				entityManager.add(spawn);
			}
		}
	}
	
	private Entity emit(final Entity entity) {
		EmitPart emitPart = entity.get(EmitPart.class);
		emitPart.reset();
		Entity spawn = entityCache.create(emitPart.getEntityType());
		TransformPart transformPart = entity.get(TransformPart.class);
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		spawnTransform.setRotation(transformPart.getRotation());
		Vector2 localSpawnPosition = emitPart.getLocalSpawnPosition();
		Vector2 spawnPosition = PolygonUtils.toGlobal(localSpawnPosition, transformPart.getPolygon());
		spawnTransform.setPosition(spawnPosition);
		//TODO: spawnTransform.setCenter(spawnPosition);
		return spawn;
	}

}
