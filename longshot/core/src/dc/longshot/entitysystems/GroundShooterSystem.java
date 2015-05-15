package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySpawner;
import dc.longshot.epf.EntitySystem;
import dc.longshot.level.LevelUtils;
import dc.longshot.parts.GroundShooterPart;
import dc.longshot.parts.WeaponPart;

public final class GroundShooterSystem extends EntitySystem {
	
	private final EntitySpawner entitySpawner;
	private final Rectangle boundsBox;
	
	public GroundShooterSystem(final EntitySpawner entitySpawner, final Rectangle boundsBox) {
		this.entitySpawner = entitySpawner;
		this.boundsBox = boundsBox;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(GroundShooterPart.class)) {
			WeaponPart weaponPart = entity.get(WeaponPart.class);
			if (weaponPart.canSpawn()) {
				Entity spawn = LevelUtils.createWeaponSpawn(entity, entitySpawner);
				LevelUtils.setupBottomDestination(spawn, boundsBox);
			}
		}
	}

}
