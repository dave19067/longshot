package dc.longshot.parts;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.geometry.Bound;

@XmlRootElement
public final class BoundsRemovePart {

	@XmlElementWrapper
	private List<Bound> bounds;
	@XmlElement
	private boolean isPartial;
	
	public BoundsRemovePart() {
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
