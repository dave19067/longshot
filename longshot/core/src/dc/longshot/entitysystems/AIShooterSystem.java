package dc.longshot.entitysystems;

import com.badlogic.gdx.math.MathUtils;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.AIShooterPart;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WeaponPart;
import dc.longshot.util.VectorUtils;

public class AIShooterSystem implements EntitySystem {
	
	private EntityManager entityManager;
	
	public AIShooterSystem(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void update(float delta, Entity entity) {
		// Shoot automatically using AI
		if (entity.has(AIShooterPart.class)) {
			AIShooterPart aiShooterPart = entity.get(AIShooterPart.class);
			if (MathUtils.random(aiShooterPart.getShootRate()) < delta) {
				WeaponPart weaponPart = entity.get(WeaponPart.class);
				if (weaponPart.canSpawn()) {
					for (Entity other : entityManager.getAll()) {
						if (other != entity && other.has(AlliancePart.class) && other.get(AlliancePart.class).getAlliance()
								== aiShooterPart.getTargetAlliance()) {
							Entity spawn = weaponPart.createSpawn();
							TransformPart spawnTransform = spawn.get(TransformPart.class);
							TransformPart transform = entity.get(TransformPart.class);
							spawnTransform.setPosition(VectorUtils.relativeCenter(transform.getCenter(), 
									spawnTransform.getBoundingSize()));
							TransformPart otherTransform = other.get(TransformPart.class);
							spawn.get(TranslatePart.class).setVelocity(otherTransform.getCenter().sub(
									spawnTransform.getCenter()));
							entityManager.add(spawn);
						}
					}
				}
			}
		}
	}

}
