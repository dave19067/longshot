package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class SpeedPart extends Part {

	private float speed;
	
	public SpeedPart(final float speed) {
		this.speed = speed;
	}
	
	public final float getSpeed() {
		return speed;
	}
	
	public final void setSpeed(final float speed) {
		this.speed = speed;
	}
	
}
