package dc.longshot.entitysystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.models.Alliance;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.TranslatePart;

public final class InputMovementSystem implements EntitySystem {

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(AlliancePart.class, TranslatePart.class)) {
			if (entity.get(AlliancePart.class).getAlliance() == Alliance.PLAYER) {
				Vector2 moveDirection = new Vector2(0, 0);
				if (Gdx.input.isKeyPressed(Keys.A)) {
					moveDirection.x -= 1;
				}
				if (Gdx.input.isKeyPressed(Keys.S)) {
					moveDirection.x += 1;
				}	
				entity.get(TranslatePart.class).setVelocity(moveDirection);
			}
		}
	}

}
