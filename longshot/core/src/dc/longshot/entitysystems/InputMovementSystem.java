package dc.longshot.entitysystems;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.game.EntityUtils;
import dc.longshot.models.Alliance;
import dc.longshot.models.InputAction;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.TranslatePart;

public final class InputMovementSystem extends EntitySystem {
	
	private final Map<InputAction, Integer> inputActions;
	
	public InputMovementSystem(final Map<InputAction, Integer> inputActions) {
		this.inputActions = inputActions;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(AlliancePart.class, TranslatePart.class)) {
			if (entity.get(AlliancePart.class).getAlliance() == Alliance.PLAYER) {
				Vector2 moveDirection = new Vector2(0, 0);
				if (isPerformed(InputAction.LEFT)) {
					moveDirection.x -= 1;
				}
				if (isPerformed(InputAction.RIGHT)) {
					moveDirection.x += 1;
				}
				EntityUtils.setDirection(entity, moveDirection);
			}
		}
	}
	
	private boolean isPerformed(final InputAction inputAction) {
		return Gdx.input.isKeyPressed(inputActions.get(inputAction));
	}

}
