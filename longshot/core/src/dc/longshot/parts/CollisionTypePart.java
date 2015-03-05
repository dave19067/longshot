package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.models.CollisionType;

@XmlRootElement
public final class CollisionTypePart {

	@XmlElement
	private CollisionType collisionType;
	
	public CollisionTypePart() {
	}
	
	public final CollisionType getCollisionType() {
		return collisionType;
	}
	
}
