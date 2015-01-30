package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dc.longshot.geometry.Bound;

public final class BoundsRemovePart {
	
	private final List<Bound> bounds;
	private final boolean isPartial;
	
	public BoundsRemovePart() {
		this(new ArrayList<Bound>(Arrays.asList(Bound.BOTTOM, Bound.LEFT, Bound.RIGHT, Bound.TOP)), true);
	}
	
	public BoundsRemovePart(final List<Bound> bounds, final boolean isPartial) {
		this.bounds = bounds;
		this.isPartial = isPartial;
	}
	
	public final List<Bound> getBounds() {
		return new ArrayList<Bound>(bounds);
	}
	
	/**
	 * @return if only part of the entity has to violate the bounds to be considered out of bounds
	 */
	public final boolean isPartial() {
		return isPartial;
	}
	
}
