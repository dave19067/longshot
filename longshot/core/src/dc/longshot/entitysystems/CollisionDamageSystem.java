package dc.longshot.entitysystems;

import java.util.List;

import dc.longshot.collision.CollisionManager;
import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.models.CollisionType;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.HealthPart;

public final class CollisionDamageSystem extends EntitySystem {
	
	private final CollisionManager collisionManager;

	public CollisionDamageSystem(final CollisionManager collisionManager) {
		this.collisionManager = collisionManager;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		// Go through collisions
		for (Entity other : collisionManager.getCollisions(entity)) {
			if (other.isActive() && other.hasActive(CollisionTypePart.class, HealthPart.class)) {
				CollisionTypePart otherCollisionTypePart = other.get(CollisionTypePart.class);
				if (entity.hasActive(DamageOnCollisionPart.class)) {
					// Damage the other entity
					DamageOnCollisionPart damageOnCollisionPart = entity.get(DamageOnCollisionPart.class);
					List<CollisionType> damageCollisionTypes = damageOnCollisionPart.getCollisionTypes();
					if (damageCollisionTypes.contains(otherCollisionTypePart.getCollisionType())) {
						other.get(HealthPart.class).decrease(damageOnCollisionPart.getDamage());
					}
				}
			}
		}
	}
	
}
