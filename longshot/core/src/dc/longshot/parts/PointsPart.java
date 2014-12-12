package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class PointsPart extends Part {

	private final int points;
	
	public PointsPart(final int points) {
		this.points = points;
	}
	
	public final int getPoints() {
		return points;
	}
	
}
