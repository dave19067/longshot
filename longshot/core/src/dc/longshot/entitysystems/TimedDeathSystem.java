package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.TimedDeathPart;

public final class TimedDeathSystem implements EntitySystem {

	private final EntityManager entityManager;
	
	public TimedDeathSystem(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.has(TimedDeathPart.class)) {
			if (entity.get(TimedDeathPart.class).isDead()) {
				entityManager.remove(entity);
			}
		}
	}

}
