package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.TimedDeathPart;

public final class TimedDeathSystem extends EntitySystem {

	private final EntityManager entityManager;
	
	public TimedDeathSystem(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.hasActive(TimedDeathPart.class)) {
			TimedDeathPart timedDeathPart = entity.get(TimedDeathPart.class);
			timedDeathPart.update(delta);
			if (timedDeathPart.isDead()) {
				entityManager.remove(entity);
			}
		}
	}

}
