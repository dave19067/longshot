package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.models.Alliance;

public class AIShooterPart extends Part {
	
	private float shootRate;
	private Alliance targetAlliance;
	
	public AIShooterPart(float shootRate, Alliance targetAlliance) {
		this.shootRate = shootRate;
		this.targetAlliance = targetAlliance;
	}
	
	public float getShootRate() {
		return shootRate;
	}
	
	public Alliance getTargetAlliance() {
		return targetAlliance;
	}

}
