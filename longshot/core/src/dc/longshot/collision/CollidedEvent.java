package dc.longshot.collision;

import dc.longshot.epf.Entity;
import dc.longshot.eventmanagement.Event;

public final class CollidedEvent implements Event<CollidedListener> {

	private final Entity e1;
	private final Entity e2;
	
	public CollidedEvent(final Entity e1, final Entity e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@Override
	public final void notify(final CollidedListener listener) {
		listener.executed(e1, e2);
	}

}
