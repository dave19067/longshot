package dc.longshot.epf;

import dc.longshot.parts.GhostPart;
import dc.longshot.parts.HealthPart;

public class NoHealthSystem implements EntitySystem {

	private EntityManager entityManager;
	
	public NoHealthSystem(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public void update(float delta, Entity entity) {
		// Remove if no health
		if (entity.has(HealthPart.class) && !entity.get(HealthPart.class).isAlive()) {
			if (entity.has(GhostPart.class)) {
				entity.get(GhostPart.class).activate();
			}
			else {
				entityManager.remove(entity);
			}
		}
	}

}
