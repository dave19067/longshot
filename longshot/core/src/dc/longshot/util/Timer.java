package dc.longshot.util;

public final class Timer {

	private float maxTime;
	private float time = 0;
	
	public Timer() {
	}
	
	public Timer(final float maxTime) {
		this.maxTime = maxTime;
	}
	
	public final float getMaxTime() {
		return maxTime;
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
