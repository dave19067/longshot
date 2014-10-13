package dc.longshot.geometry;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public enum Bound {
	
	TOP, LEFT, RIGHT, BOTTOM;
	
	public static final boolean isOutOfBounds(final Rectangle collisionBox, final Rectangle boundsBox, 
			final List<Bound> bounds) {
		for (Bound bound : checkOutOfBounds(collisionBox, boundsBox)) {
			if (bounds.contains(bound)) {
				return true;
			}
		}
		return false;
	}
	
	public static final List<Bound> checkOutOfBounds(final Rectangle collisionBox, final Rectangle boundsBox) {
		List<Bound> bounds = new ArrayList<Bound>();
		if (collisionBox.x < boundsBox.x) {
			bounds.add(Bound.LEFT);
		}
		else if (collisionBox.x + collisionBox.width > boundsBox.x + boundsBox.width) {
			bounds.add(Bound.RIGHT);
		}
		if (collisionBox.y < boundsBox.y) {
			bounds.add(Bound.BOTTOM);
		}
		else if (collisionBox.y + collisionBox.height > boundsBox.y + boundsBox.height) {
			bounds.add(Bound.TOP);
		}
		return bounds;
	}
	
}
