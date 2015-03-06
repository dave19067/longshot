package dc.longshot.parts;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dc.longshot.util.Timer;
import dc.longshot.xmladapters.TimerAdapter;

@XmlRootElement
public final class TimedDeathPart {

	@XmlJavaTypeAdapter(TimerAdapter.class)
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
