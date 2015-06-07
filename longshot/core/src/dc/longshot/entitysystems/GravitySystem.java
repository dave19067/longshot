package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.GravityPart;
import dc.longshot.parts.TranslatePart;

public final class GravitySystem extends EntitySystem {


	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(GravityPart.class)) {
			TranslatePart translatePart = entity.get(TranslatePart.class);
			Vector2 velocity = translatePart.getVelocity();
			// Gravity acceleration in world units/s^2
			final float acceleration = -20;
			velocity.y += acceleration * delta;
			translatePart.setVelocity(velocity);
		}
	}
	
}
