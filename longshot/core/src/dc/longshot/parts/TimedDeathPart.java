package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.util.Timer;

@XmlRootElement
public final class TimedDeathPart {

	@XmlElement
	private Timer deathTimer;
	
	public TimedDeathPart() {
	}
	
	public TimedDeathPart(final float deathTime) {
		deathTimer = new Timer(deathTime);
	}
	
	public final boolean isDead() {
		return deathTimer.isElapsed();
	}

	public final void update(final float delta) {
		deathTimer.tick(delta);
	}
	
}
