package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dc.longshot.epf.Part;
import dc.longshot.models.Bound;

public class BouncePart extends Part {

	private List<Bound> bounds;
	
	public BouncePart() {
		this(new ArrayList<Bound>(Arrays.asList(Bound.BOTTOM, Bound.LEFT, Bound.RIGHT, Bound.TOP)));
	}
	
	public BouncePart(List<Bound> bounds) {
		this.bounds = bounds;
	}
	
	public List<Bound> getBounds() {
		return new ArrayList<Bound>(bounds);
	}
	
}
