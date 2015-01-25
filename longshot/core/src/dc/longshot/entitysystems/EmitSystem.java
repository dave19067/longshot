package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.level.EntityFactory;
import dc.longshot.parts.EmitPart;
import dc.longshot.parts.TransformPart;

public final class EmitSystem extends EntitySystem {

	private final EntityManager entityManager;
	private final EntityFactory entityFactory;
	
	public EmitSystem(final EntityManager entityManager, final EntityFactory entityFactory) {
		this.entityManager = entityManager;
		this.entityFactory = entityFactory;
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
		Entity spawn = entityFactory.create(emitPart.getEntityType());
		TransformPart transformPart = entity.get(TransformPart.class);
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		spawnTransform.setRotation(transformPart.getRotation());
		Vector2 localSpawnPosition = emitPart.getLocalSpawnPosition();
		Vector2 spawnPosition = PolygonUtils.toGlobal(localSpawnPosition, transformPart.getPolygon());
		spawnTransform.setCenter(spawnPosition);
		return spawn;
	}

}
