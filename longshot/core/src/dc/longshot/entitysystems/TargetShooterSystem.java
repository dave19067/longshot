package dc.longshot.entitysystems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.game.EntityUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.level.EntityFactory;
import dc.longshot.level.LevelUtils;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.TargetShooterPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WeaponPart;

public final class TargetShooterSystem extends EntitySystem {
	
	private final EntityManager entityManager;
	private final EntityFactory entityFactory;
	
	public TargetShooterSystem(final EntityManager entityManager, final EntityFactory entityFactory) {
		this.entityManager = entityManager;
		this.entityFactory = entityFactory;
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
	
	private void spawn(final Entity entity, final Entity target) {
		Entity spawn = LevelUtils.createWeaponSpawn(entity, entityFactory);
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		TransformPart otherTransform = target.get(TransformPart.class);
		Vector2 direction = VectorUtils.offset(spawnTransform.getCenter(), otherTransform.getCenter());
		EntityUtils.setDirection(spawn, direction);
		entityManager.add(spawn);
	}

}
