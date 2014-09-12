package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public class ShotStatsPart extends Part {

	private float distanceTraveled = 0;
	private int bounceNum = 0;
	private Vector2 oldPosition;
	
	public float getDistanceTraveled() {
		return distanceTraveled;
	}
	
	public int getBounceNum() {
		return bounceNum;
	}
	
	public void setBounceNum(int bounceNum) {
		this.bounceNum = bounceNum;
	}
	
	@Override
	public void initialize() {
		oldPosition = entity.get(TransformPart.class).getPosition();
	}
	
	@Override
	public void update(float delta) {
		Vector2 newPosition = entity.get(TransformPart.class).getPosition();
		distanceTraveled += (newPosition.cpy().sub(oldPosition).len());
		oldPosition = newPosition.cpy();
	}
	
}
