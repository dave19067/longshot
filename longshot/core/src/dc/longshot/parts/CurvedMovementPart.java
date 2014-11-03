package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class CurvedMovementPart extends Part {
	
	private final float curveSize;
	
	public CurvedMovementPart(final float curveSize) {
		this.curveSize = curveSize;
	}
	
	public final float getCurveSize() {
		return curveSize;
	}
	
}
