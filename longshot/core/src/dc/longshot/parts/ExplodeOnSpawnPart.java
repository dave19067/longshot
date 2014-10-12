package dc.longshot.parts;

import dc.longshot.epf.Part;

public class ExplodeOnSpawnPart extends Part {

	private float radius;
	private float damage;
	
	public ExplodeOnSpawnPart(float radius, float damage) {
		this.radius = radius;
		this.damage = damage;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float getDamage() {
		return damage;
	}
	
}
