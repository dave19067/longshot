package dc.longshot.parts;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.geometry.Bound;

@XmlRootElement
public final class BoundsPart {

	@XmlElementWrapper
	private List<Bound> bounds;
	
	public BoundsPart() {
	}

	public final List<Bound> getBounds() {
		return new ArrayList<Bound>(bounds);
	}
	
}
