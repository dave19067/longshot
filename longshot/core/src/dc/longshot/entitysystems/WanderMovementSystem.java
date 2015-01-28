package dc.longshot.entitysystems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.game.EntityUtils;
import dc.longshot.parts.WanderMovementPart;

public final class WanderMovementSystem extends EntitySystem {

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(WanderMovementPart.class)) {
			act(entity);
		}
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(WanderMovementPart.class)) {
			float decisionRate = entity.get(WanderMovementPart.class).getDecisionRate();
			if (MathUtils.randomBoolean(delta / decisionRate)) {
				act(entity);
			}
		}
	}
	
	private void act(final Entity entity) {
		float idleToMoveRatio = entity.get(WanderMovementPart.class).getIdleToMoveRatio();
		Vector2 direction;
		if (MathUtils.randomBoolean(idleToMoveRatio / (idleToMoveRatio + 1))) {
			// Idle
			direction = new Vector2(0, 0);
		}
		else {
			// Move
			direction = new Vector2(1, 0);
			direction.setAngle(MathUtils.random(0, 360));
		}
		EntityUtils.setDirection(entity, direction);
	}

}
