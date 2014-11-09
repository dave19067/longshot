package dc.longshot.entitysystems;

import com.badlogic.gdx.math.MathUtils;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.parts.AIShooterPart;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WeaponPart;

public final class AIShooterSystem implements EntitySystem {
	
	private final EntityManager entityManager;
	
	public AIShooterSystem(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(AIShooterPart.class, WeaponPart.class)) {
			AIShooterPart aiShooterPart = entity.get(AIShooterPart.class);
			if (MathUtils.random(aiShooterPart.getShootRate()) < delta) {
				WeaponPart weaponPart = entity.get(WeaponPart.class);
				if (weaponPart.canSpawn()) {
					for (Entity other : entityManager.getAll()) {
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
		TransformPart transform = entity.get(TransformPart.class);
		spawnTransform.setPosition(PolygonUtils.relativeCenter(transform.getGlobalCenter(), 
				spawnTransform.getBoundingSize()));
		TransformPart otherTransform = other.get(TransformPart.class);
		spawn.get(TranslatePart.class).setVelocity(otherTransform.getGlobalCenter().sub(
				spawnTransform.getGlobalCenter()));
		entityManager.add(spawn);
	}

}
