package dc.longshot.parts;

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;

public final class HealthPart {

	private final EventDelegate<NoArgsListener> noHealthDelegate = new EventDelegate<NoArgsListener>();
	
	private final float maxHealth;
	private float health;
	
	public HealthPart(final float maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	
	public final void addNoHealthListener(final NoArgsListener listener) {
		noHealthDelegate.listen(listener);
	}
	
	public final void reset() {
		health = maxHealth;
	}
	
	public final void decrease(final float value) {
		health -= value;
		if (health <= 0) {
			noHealthDelegate.notify(new NoArgsEvent());
		}
	}
	
}
