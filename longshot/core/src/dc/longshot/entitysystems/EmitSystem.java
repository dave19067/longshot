package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.EmitterPart;

public class EmitSystem implements EntitySystem {

	private EntityManager entityManager;
	
	public EmitSystem(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public void update(float delta, Entity entity) {
		// Emit
		if (entity.has(EmitterPart.class)) {
			EmitterPart emitterPart = entity.get(EmitterPart.class);
			if (emitterPart.canEmit()) {
				Entity spawn = emitterPart.emit();
				entityManager.add(spawn);
			}
		}
	}

}
