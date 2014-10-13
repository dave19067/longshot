package dc.longshot.models;

public final class Session {

	private boolean isRunning = true;
	
	// TODO: replace with libgdx screen pause
	public final boolean isRunning() {
		return isRunning;
	}
	
	public final void setRunning(final boolean isRunning) {
		this.isRunning = isRunning;
	}
	
}
