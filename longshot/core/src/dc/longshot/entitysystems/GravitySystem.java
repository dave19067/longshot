package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.GravityPart;
import dc.longshot.parts.TranslatePart;

public final class GravitySystem extends EntitySystem {

	/**
	 * Gravity acceleration in world units/s^2.
	 */
	private static final float ACCELERATION = -20;

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(GravityPart.class)) {
			TranslatePart translatePart = entity.get(TranslatePart.class);
			Vector2 velocity = translatePart.getVelocity();
			velocity.y += ACCELERATION * delta;
			translatePart.setVelocity(velocity);
		}
	}
	
}
