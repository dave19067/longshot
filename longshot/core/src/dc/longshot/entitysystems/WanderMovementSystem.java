package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.WanderMovementPart;

public final class WanderMovementSystem extends EntitySystem {

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(WanderMovementPart.class)) {
			entity.get(WanderMovementPart.class).act();
		}
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(WanderMovementPart.class)) {
			entity.get(WanderMovementPart.class).update(delta);
		}
	}

}
