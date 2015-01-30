package dc.longshot.parts;

public final class DamageOnSpawnPart {

	private final float radius;
	private final float damage;
	
	public DamageOnSpawnPart(final float radius, final float damage) {
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
