package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class TimedDeathPart extends Part {

	private float deathTime;
	
	public TimedDeathPart(float deathTime) {
		this.deathTime = deathTime;
	}
	
	public final boolean isDead() {
		return deathTime <= 0;
	}

	@Override
	public final void update(final float delta) {
		deathTime -= delta;
	}
	
}
