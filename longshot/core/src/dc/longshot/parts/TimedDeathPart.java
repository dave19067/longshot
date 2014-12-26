package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.util.Timer;

public final class TimedDeathPart extends Part {

	private final Timer deathTimer;
	
	public TimedDeathPart(final float deathTime) {
		deathTimer = new Timer(deathTime);
	}
	
	public final boolean isDead() {
		return deathTimer.isElapsed();
	}

	@Override
	public final void update(final float delta) {
		deathTimer.tick(delta);
	}
	
}
