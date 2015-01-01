package dc.longshot.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public final class LevelUtils {

	private LevelUtils() {
	}
	
	public static final void setupBottomDestination(final Entity entity, final Rectangle boundsBox) {
		TransformPart transformPart = entity.get(TransformPart.class);
		
		// Get the destination, which is a random point on the ground
		float destX = MathUtils.random(boundsBox.x, PolygonUtils.right(boundsBox) - transformPart.getSize().x);
		Vector2 destPosition = new Vector2(destX, 0);
		
		// Find the direction to get from the entity spawn position to the destination
		Vector2 offset = VectorUtils.offset(transformPart.getPosition(), destPosition);
		TranslatePart translate = entity.get(TranslatePart.class);
		translate.setDirection(offset);
	}
	
}
