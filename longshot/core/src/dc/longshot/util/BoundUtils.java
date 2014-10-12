package dc.longshot.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.models.Bound;

public class BoundUtils {
	
	public static boolean isOutOfBounds(Rectangle collisionBox, Rectangle boundsBox, List<Bound> bounds) {
		for (Bound bound : checkOutOfBounds(collisionBox, boundsBox)) {
			if (bounds.contains(bound)) {
				return true;
			}
		}
		return false;
	}
	
	public static List<Bound> checkOutOfBounds(Rectangle collisionBox, Rectangle boundsBox) {
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
