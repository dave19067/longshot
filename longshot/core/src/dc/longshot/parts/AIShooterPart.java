package dc.longshot.parts;

import dc.longshot.epf.Part;

public class AIShooterPart extends Part {
	
	private float shootRate;
	
	public AIShooterPart(float shootRate) {
		this.shootRate = shootRate;
	}
	
	public float getShootRate() {
		return shootRate;
	}

}
