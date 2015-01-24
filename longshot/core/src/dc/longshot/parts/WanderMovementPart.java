package dc.longshot.parts;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public final class WanderMovementPart extends Part {

	private final float decisionRate;
	private final float idleToMoveRatio;
	
	/**
	 * Constructor.
	 * @param decisionRate average number of seconds per decision
	 * @param idleToMoveRatio how often to stay in place than move on a decision
	 */
	public WanderMovementPart(final float decisionRate, final float idleToMoveRatio) {
		this.decisionRate = decisionRate;
		this.idleToMoveRatio = idleToMoveRatio;
	}
	
	public final void update(final float delta) {
		if (MathUtils.randomBoolean(delta / decisionRate)) {
			act();
		}
	}
	
	public final void act() {
		TranslatePart translatePart = entity.get(TranslatePart.class);
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
		translatePart.setDirection(direction);
	}
	
}
