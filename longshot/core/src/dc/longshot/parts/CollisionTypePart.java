package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.models.CollisionType;

public class CollisionTypePart extends Part {

	private CollisionType collisionType;
	
	public CollisionTypePart(CollisionType collisionType) {
		this.collisionType = collisionType;
	}
	
	public CollisionType getCollisionType() {
		return collisionType;
	}
	
}
