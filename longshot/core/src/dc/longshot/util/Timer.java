package dc.longshot.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// TODO: Create adapter for timer
@XmlRootElement
public final class Timer {

	@XmlElement
	private float maxTime;
	private float time = 0;
	
	public Timer() {
	}
	
	public Timer(final float maxTime) {
		this.maxTime = maxTime;
	}
	
	public final boolean isElapsed() {
		return time >= maxTime;
	}
	
	public final float getElapsedPercent() {
		return time / maxTime;
	}
	
	public final void reset() {
		time = 0;
	}
	
	public final void tick(final float delta) {
		time += delta;
	}
	
}
