package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WeaponPart;

public final class ShooterInputSystem implements EntitySystem {
	
	private final EntityManager entityManager;
	
	public ShooterInputSystem(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (entity.hasActive(WeaponPart.class, AttachmentPart.class)) {
				WeaponPart weaponPart = entity.get(WeaponPart.class);
				if (weaponPart.canSpawn()) {
					Entity bullet = weaponPart.createSpawn();
					Entity cannon = entity.get(AttachmentPart.class).getAttachedEntity();
					Vector2 spawnPosition = getMiddleOfCannonMouth(cannon, bullet);
					bullet.get(TransformPart.class).setPosition(spawnPosition);
					Vector2 velocity = VectorUtils.createVectorFromAngle(
							cannon.get(TransformPart.class).getRotation());
					bullet.get(TranslatePart.class).setVelocity(velocity);
					entityManager.add(bullet);
				}
			}
		}
	}
	
	private Vector2 getMiddleOfCannonMouth(final Entity cannon, final Entity spawn) {
		TransformPart cannonTransform = cannon.get(TransformPart.class);
		List<Vector2> vertices = cannonTransform.getTransformedVertices();
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		Vector2 spawnPosition = VectorUtils.relativeEdgeMiddle(vertices.get(1), vertices.get(2), 
				spawnTransform.getSize().y);
		return spawnPosition;
	}

}
