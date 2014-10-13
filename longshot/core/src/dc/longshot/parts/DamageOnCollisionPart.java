package dc.longshot.parts;

import java.util.List;

import dc.longshot.epf.Part;
import dc.longshot.models.CollisionType;

public final class DamageOnCollisionPart extends Part {

	private final List<CollisionType> collisionTypes;
	private final float damage;
	
	public DamageOnCollisionPart(final List<CollisionType> collisionTypes, final float damage) {
		this.collisionTypes = collisionTypes;
		this.damage = damage;
	}
	
	public final List<CollisionType> getCollisionTypes() {
		return collisionTypes;
	}
	
	public final float getDamage() {
		return damage;
	}

}
