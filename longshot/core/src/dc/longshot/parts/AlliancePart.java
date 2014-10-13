package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.models.Alliance;

public final class AlliancePart extends Part {

	private final Alliance alliance;
	
	public AlliancePart(final Alliance alliance) {
		this.alliance = alliance;
	}
	
	public final Alliance getAlliance() {
		return alliance;
	}
	
}
