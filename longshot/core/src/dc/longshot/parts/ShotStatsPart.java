package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public final class ShotStatsPart extends Part {

	private float distanceTraveled = 0;
	private int bounceNum = 0;
	private Vector2 oldPosition;
	
	public final float getDistanceTraveled() {
		return distanceTraveled;
	}
	
	public final int getBounceNum() {
		return bounceNum;
	}
	
	public final void setBounceNum(final int bounceNum) {
		this.bounceNum = bounceNum;
	}
	
	@Override
	public final void initialize() {
		oldPosition = entity.get(TransformPart.class).getPosition();
	}
	
	@Override
	public final void update(final float delta) {
		Vector2 newPosition = entity.get(TransformPart.class).getPosition();
		distanceTraveled += (newPosition.cpy().sub(oldPosition).len());
		oldPosition = newPosition.cpy();
	}
	
}
