package dc.longshot.geometry;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public enum Bound {
	
	LEFT, RIGHT, BOTTOM, TOP;

	public static final boolean isOutOfBounds(final Rectangle collisionBox, final Rectangle boundsBox, 
			final float buffer) {
		return getViolatedBounds(collisionBox, boundsBox, buffer).size() > 0;
	}
	
	public static final boolean isOutOfBounds(final Rectangle collisionBox, final Rectangle boundsBox, 
			final List<Bound> checkedBounds) {
		for (Bound bound : getViolatedBounds(collisionBox, boundsBox)) {
			if (checkedBounds.contains(bound)) {
				return true;
			}
		}
		return false;
	}
	
	public static final List<Bound> getViolatedBounds(final Rectangle collisionBox, final Rectangle boundsBox) {
		return getViolatedBounds(collisionBox, boundsBox, 0);
	}
	
	private static final List<Bound> getViolatedBounds(final Rectangle collisionBox, final Rectangle boundsBox, 
			final float buffer) {
		List<Bound> bounds = new ArrayList<Bound>();
		if (collisionBox.x < boundsBox.x + buffer) {
			bounds.add(Bound.LEFT);
		}
		else if (PolygonUtils.right(collisionBox) > PolygonUtils.right(boundsBox) - buffer) {
			bounds.add(Bound.RIGHT);
		}
		if (collisionBox.y < boundsBox.y + buffer) {
			bounds.add(Bound.BOTTOM);
		}
		else if (PolygonUtils.top(collisionBox) > PolygonUtils.top(boundsBox) - buffer) {
			bounds.add(Bound.TOP);
		}
		return bounds;
	}
	
}
