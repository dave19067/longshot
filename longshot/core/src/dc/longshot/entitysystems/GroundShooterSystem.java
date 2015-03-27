package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityCache;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.level.LevelUtils;
import dc.longshot.parts.GroundShooterPart;
import dc.longshot.parts.WeaponPart;

public final class GroundShooterSystem extends EntitySystem {
	
	private final EntityCache entityCache;
	private final EntityManager entityManager;
	private final Rectangle boundsBox;
	
	public GroundShooterSystem(final EntityCache entityCache, final EntityManager entityManager, 
			final Rectangle boundsBox) {
		this.entityCache = entityCache;
		this.entityManager = entityManager;
		this.boundsBox = boundsBox;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(GroundShooterPart.class)) {
			WeaponPart weaponPart = entity.get(WeaponPart.class);
			if (weaponPart.canSpawn()) {
				Entity spawn = LevelUtils.createWeaponSpawn(entity, entityCache);
				LevelUtils.setupBottomDestination(spawn, boundsBox);
				entityManager.add(spawn);
			}
		}
	}

}
