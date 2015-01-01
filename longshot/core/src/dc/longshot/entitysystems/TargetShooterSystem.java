package dc.longshot.entitysystems;

import com.badlogic.gdx.math.MathUtils;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.TargetShooterPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WeaponPart;

public final class TargetShooterSystem implements EntitySystem {
	
	private final EntityManager entityManager;
	
	public TargetShooterSystem(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(TargetShooterPart.class)) {
			TargetShooterPart aiShooterPart = entity.get(TargetShooterPart.class);
			if (MathUtils.randomBoolean(delta / aiShooterPart.getShootRate())) {
				WeaponPart weaponPart = entity.get(WeaponPart.class);
				if (weaponPart.canSpawn()) {
					for (Entity other : entityManager.getManaged()) {
						if (other != entity && other.hasActive(AlliancePart.class)) {
							if (other.get(AlliancePart.class).getAlliance() == aiShooterPart.getTargetAlliance()) {
								spawn(entity, other);
							}
						}
					}
				}
			}
		}
	}
	
	private void spawn(Entity entity, Entity other) {
		Entity spawn = entity.get(WeaponPart.class).createSpawn();
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		TransformPart otherTransform = other.get(TransformPart.class);
		spawn.get(TranslatePart.class).setDirection(VectorUtils.offset(spawnTransform.getCenter(), 
				otherTransform.getCenter()));
		entityManager.add(spawn);
	}

}
