package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySpawner;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.parts.EmitPart;
import dc.longshot.parts.TransformPart;

public final class EmitSystem extends EntitySystem {

	private final EntitySpawner entitySpawner;
	
	public EmitSystem(final EntitySpawner entitySpawner) {
		this.entitySpawner = entitySpawner;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(EmitPart.class)) {
			EmitPart emitPart = entity.get(EmitPart.class);
			emitPart.update(delta);
			if (emitPart.canEmit()) {
				emit(entity);
			}
		}
	}
	
	private void emit(final Entity entity) {
		EmitPart emitPart = entity.get(EmitPart.class);
		emitPart.reset();
		Entity spawn = entitySpawner.spawn(emitPart.getEntityType());
		TransformPart transformPart = entity.get(TransformPart.class);
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		spawnTransform.setRotation(transformPart.getRotation());
		Vector2 localSpawnPosition = emitPart.getLocalSpawnPosition();
		Vector2 spawnPosition = PolygonUtils.toGlobal(localSpawnPosition, transformPart.getPolygon());
		spawnTransform.setCenter(spawnPosition);
	}

}
