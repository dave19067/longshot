package dc.longshot.entitysystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityCache;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.game.EntityUtils;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.level.LevelUtils;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WeaponPart;

public final class ShooterInputSystem extends EntitySystem {

	private final EntityCache entityCache;
	private final EntityManager entityManager;
	
	public ShooterInputSystem(final EntityCache entityCache, final EntityManager entityManager) {
		this.entityCache = entityCache;
		this.entityManager = entityManager;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(WeaponPart.class, AttachmentPart.class)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				WeaponPart weaponPart = entity.get(WeaponPart.class);
				if (weaponPart.canSpawn()) {
					Entity bullet =  LevelUtils.createWeaponSpawn(entity, entityCache);
					Entity cannon = entity.get(AttachmentPart.class).getAttachedEntity();
					Vector2 spawnPosition = getMiddleOfCannonMouth(cannon, bullet);
					bullet.get(TransformPart.class).setPosition(spawnPosition);
					Vector2 direction = VectorUtils.fromAngle(
							cannon.get(TransformPart.class).getRotation());
					EntityUtils.setDirection(bullet, direction);
					entityManager.add(bullet);
				}
			}
		}
	}
	
	private Vector2 getMiddleOfCannonMouth(final Entity cannon, final Entity spawn) {
		TransformPart cannonTransform = cannon.get(TransformPart.class);
		Vector2 size = cannonTransform.getSize();
		Vector2 spawnPosition = PolygonUtils.toGlobal(size.x, size.y / 2, cannonTransform.getPolygon());
		return PolygonUtils.relativeCenter(spawnPosition, spawn.get(TransformPart.class).getSize());
	}

}
