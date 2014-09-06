package dc.longshot.parts;

import dc.longshot.epf.Part;

public class TimedDeathPart extends Part {

	private float deathTime;
	
	public TimedDeathPart(float deathTime) {
		this.deathTime = deathTime;
	}
	
	public boolean isDead() {
		return deathTime <= 0;
	}

	@Override
	public void update(float delta) {
		deathTime -= delta;
	}
	
}
