package dc.longshot.parts;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.geometry.Bound;

@XmlRootElement
public final class BouncePart {

	@XmlElementWrapper
	private List<Bound> bounds;
	
	public BouncePart() {
	}
	
	public final List<Bound> getBounds() {
		return new ArrayList<Bound>(bounds);
	}
	
}
