package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.GhostPart;
import dc.longshot.parts.HealthPart;

public final class NoHealthSystem implements EntitySystem {

	private final EntityManager entityManager;
	
	public NoHealthSystem(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(HealthPart.class) && !entity.get(HealthPart.class).isAlive()) {
			if (entity.hasActive(GhostPart.class)) {
				entity.get(GhostPart.class).activate();
			}
			else {
				entityManager.remove(entity);
			}
		}
	}

}
