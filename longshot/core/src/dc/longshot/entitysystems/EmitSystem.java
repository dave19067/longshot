package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.EmitterPart;

public final class EmitSystem extends EntitySystem {

	private final EntityManager entityManager;
	
	public EmitSystem(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(EmitterPart.class)) {
			EmitterPart emitterPart = entity.get(EmitterPart.class);
			emitterPart.update(delta);
			if (emitterPart.canEmit()) {
				Entity spawn = emitterPart.emit();
				entityManager.add(spawn);
			}
		}
	}

}
