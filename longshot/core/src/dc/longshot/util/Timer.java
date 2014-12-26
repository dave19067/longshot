package dc.longshot.util;

public final class Timer {

	private final float maxTime;
	private float time = 0;
	
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
