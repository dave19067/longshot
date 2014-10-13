package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.models.CollisionType;

public final class CollisionTypePart extends Part {

	private final CollisionType collisionType;
	
	public CollisionTypePart(final CollisionType collisionType) {
		this.collisionType = collisionType;
	}
	
	public final CollisionType getCollisionType() {
		return collisionType;
	}
	
}
