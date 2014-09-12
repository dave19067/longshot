package dc.longshot.parts;

import dc.longshot.epf.Part;

public class ExplodeOnSpawnPart extends Part {

	private float radius;
	
	public ExplodeOnSpawnPart(float radius) {
		this.radius = radius;
	}
	
	public float getRadius() {
		return radius;
	}
	
}
