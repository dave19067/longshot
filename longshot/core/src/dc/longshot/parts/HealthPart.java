package dc.longshot.parts;

import dc.longshot.epf.Part;

public class HealthPart extends Part {

	private float maxHealth;
	private float health;
	
	public HealthPart(float health) {
		this.maxHealth = health;
		this.health = health;
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	public void reset() {
		health = maxHealth;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public void subtract(float value) {
		health -= value;
	}
	
}
