package dc.longshot.events;

import dc.longshot.epf.Entity;

public interface CollidedListener {

	void executed(final Entity e1, final Entity e2);
	
}
