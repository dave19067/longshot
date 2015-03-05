package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class DamageOnSpawnPart {

	@XmlElement
	private float radius;
	@XmlElement
	private float damage;
	
	public DamageOnSpawnPart() {
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float getDamage() {
		return damage;
	}
	
}
