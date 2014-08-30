package dc.longshot.events;

import dc.longshot.epf.Entity;
import dc.longshot.util.Event;

public class CollidedEvent implements Event<CollidedListener> {

	private Entity e1;
	private Entity e2;
	
	public CollidedEvent(Entity e1, Entity e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@Override
	public void notify(CollidedListener listener) {
		listener.executed(e1, e2);
	}

}
