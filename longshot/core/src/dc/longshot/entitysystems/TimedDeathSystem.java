package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.TimedDeathPart;

public class TimedDeathSystem implements EntitySystem {

	private EntityManager entityManager;
	
	public TimedDeathSystem(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public void update(float delta, Entity entity) {
		// Remove if timed death
		if (entity.has(TimedDeathPart.class) && entity.get(TimedDeathPart.class).isDead()) {
			entityManager.remove(entity);
		}
	}

}
