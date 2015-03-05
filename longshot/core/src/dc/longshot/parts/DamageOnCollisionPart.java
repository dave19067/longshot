package dc.longshot.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.models.CollisionType;

@XmlRootElement
public final class DamageOnCollisionPart {

	@XmlElementWrapper
	private List<CollisionType> collisionTypes;
	@XmlElement
	private float damage;
	
	public DamageOnCollisionPart() {
	}
	
	public final List<CollisionType> getCollisionTypes() {
		return collisionTypes;
	}
	
	public final float getDamage() {
		return damage;
	}

}
