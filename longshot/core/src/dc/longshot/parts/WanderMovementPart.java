package dc.longshot.parts;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public class WanderMovementPart extends Part {

	private float decisionRate;
	private float idleToMoveRatio;
	
	/**
	 * Constructor.
	 * @param decisionRate average number of seconds per decision
	 * @param idleToMoveRatio how often to stay in place than move on a decision
	 */
	public WanderMovementPart(float decisionRate, float idleToMoveRatio) {
		this.decisionRate = decisionRate;
		this.idleToMoveRatio = idleToMoveRatio;
	}
	
	@Override
	public void initialize() {
		act();
	}
	
	@Override
	public void update(float delta) {
		if (MathUtils.random(decisionRate) < delta) {
			act();
		}
	}
	
	public void act() {
		TranslatePart translatePart = getEntity().get(TranslatePart.class);
		Vector2 direction;
		
		if (MathUtils.random(idleToMoveRatio + 1) < idleToMoveRatio) {
			// Idle
			direction = new Vector2(0, 0);
		}
		else {
			// Move
			direction = new Vector2(1, 0);
			direction.setAngle(MathUtils.random(0, 360));
		}
		
		translatePart.setVelocity(direction);
	}
	
}
