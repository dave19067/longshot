package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dc.longshot.geometry.Bound;

public final class BouncePart {

	private final List<Bound> bounds;
	
	public BouncePart() {
		this(new ArrayList<Bound>(Arrays.asList(Bound.BOTTOM, Bound.LEFT, Bound.RIGHT, Bound.TOP)));
	}
	
	public BouncePart(final List<Bound> bounds) {
		this.bounds = bounds;
	}
	
	public final List<Bound> getBounds() {
		return new ArrayList<Bound>(bounds);
	}
	
}
