package dc.longshot.events;

import dc.longshot.epf.Entity;

public interface CollidedListener {

	public void executed(final Entity e1, final Entity e2);
	
}
