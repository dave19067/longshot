package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.game.LevelUtils;
import dc.longshot.parts.GroundShooterPart;
import dc.longshot.parts.WeaponPart;

public final class GroundShooterSystem extends EntitySystem {
	
	private final EntityManager entityManager;
	private final Rectangle boundsBox;
	
	public GroundShooterSystem(final EntityManager entityManager, final Rectangle boundsBox) {
		this.entityManager = entityManager;
		this.boundsBox = boundsBox;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(GroundShooterPart.class)) {
			WeaponPart weaponPart = entity.get(WeaponPart.class);
			if (weaponPart.canSpawn()) {
				Entity spawn = weaponPart.createSpawn();
				LevelUtils.setupBottomDestination(spawn, boundsBox);
				entityManager.add(spawn);
			}
		}
	}

}
