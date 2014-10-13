package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.models.Alliance;

public final class AIShooterPart extends Part {
	
	private final float shootRate;
	private final Alliance targetAlliance;
	
	public AIShooterPart(final float shootRate, final Alliance targetAlliance) {
		this.shootRate = shootRate;
		this.targetAlliance = targetAlliance;
	}
	
	public final float getShootRate() {
		return shootRate;
	}
	
	public final Alliance getTargetAlliance() {
		return targetAlliance;
	}

}
