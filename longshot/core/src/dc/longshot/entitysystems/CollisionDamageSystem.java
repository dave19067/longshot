package dc.longshot.entitysystems;

import dc.longshot.CollisionManager;
import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.HealthPart;

public class CollisionDamageSystem implements EntitySystem {
	
	private CollisionManager collisionManager;

	public CollisionDamageSystem(CollisionManager collisionManager) {
		this.collisionManager = collisionManager;
	}
	
	@Override
	public void update(float delta, Entity entity) {
		// Go through collisions
		for (Entity other : collisionManager.getCollisions(entity)) {
			if (other.isActive() && other.has(CollisionTypePart.class)) {
				CollisionTypePart otherCollisionTypePart = other.get(CollisionTypePart.class);
				if (otherCollisionTypePart.isActive()) {
					if (entity.has(DamageOnCollisionPart.class) && other.has(HealthPart.class)) {
						// Damage the other entity
						DamageOnCollisionPart damageOnCollisionPart = entity.get(DamageOnCollisionPart.class);
						if (damageOnCollisionPart.isActive() && damageOnCollisionPart.getCollisionTypes().contains(
								otherCollisionTypePart.getCollisionType())) {
							other.get(HealthPart.class).decrease(damageOnCollisionPart.getDamage());
						}
					}
				}
			}
		}
	}
	
}
