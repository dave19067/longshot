package dc.longshot.level;

import dc.longshot.eventing.Event;

public final class LevelFinishedEvent implements Event<LevelFinishedListener> {

	private final LevelResult result;
	
	public LevelFinishedEvent(final LevelResult result) {
		this.result = result;
	}
	
	@Override
	public void notify(final LevelFinishedListener listener) {
		listener.finished(result);
	}

}
