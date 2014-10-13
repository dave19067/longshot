package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class HealthPart extends Part {

	private final float maxHealth;
	private float health;
	
	public HealthPart(final float maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	
	public final boolean isAlive() {
		return health > 0;
	}
	
	public final void reset() {
		health = maxHealth;
	}
	
	public final float getMaxHealth() {
		return maxHealth;
	}
	
	public final void decrease(final float value) {
		health -= value;
	}
	
}
