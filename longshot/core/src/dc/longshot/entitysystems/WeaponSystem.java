package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.WeaponPart;

public final class WeaponSystem extends EntitySystem {

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(WeaponPart.class)) {
			entity.get(WeaponPart.class).update(delta);
		}
	}

}
