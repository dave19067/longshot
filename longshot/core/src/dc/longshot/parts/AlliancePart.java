package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.models.Alliance;

public class AlliancePart extends Part {

	private Alliance alliance;
	
	public AlliancePart(Alliance alliance) {
		this.alliance = alliance;
	}
	
	public Alliance getAlliance() {
		return alliance;
	}
	
}
