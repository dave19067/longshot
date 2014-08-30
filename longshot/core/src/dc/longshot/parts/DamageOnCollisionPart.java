package dc.longshot.parts;

import java.util.List;

import dc.longshot.epf.Part;
import dc.longshot.models.CollisionType;

public class DamageOnCollisionPart extends Part {

	private List<CollisionType> collisionTypes;
	private float damage;
	
	public DamageOnCollisionPart(List<CollisionType> collisionTypes, float damage) {
		this.collisionTypes = collisionTypes;
		this.damage = damage;
	}
	
	public List<CollisionType> getCollisionTypes() {
		return collisionTypes;
	}
	
	public float getDamage() {
		return damage;
	}

}
