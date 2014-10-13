package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class ExplodeOnSpawnPart extends Part {

	private final float radius;
	private final float damage;
	
	public ExplodeOnSpawnPart(final float radius, final float damage) {
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
