package dc.longshot.game;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.util.FloatRange;

public final class EntityUtils {

	private EntityUtils() {
	}

	public static final void setDirection(final Entity entity, final Vector2 direction) {
		TranslatePart translatePart = entity.get(TranslatePart.class);
		Vector2 velocity;
		if (direction.equals(new Vector2(0, 0))) {
			velocity = new Vector2(0, 0);
		}
		else {
			velocity = VectorUtils.lengthened(direction, entity.get(SpeedPart.class).getSpeed());
		}
		translatePart.setVelocity(velocity);
	}
	
	public static final Vector2 calculateSize(final Vector2 originalSize, final float z, final float minZScale, 
			final FloatRange zRange) {
		float zPercent = (z - zRange.min()) / zRange.difference(); 
		float zScalePercent = minZScale + (1 - minZScale) * zPercent;
		return originalSize.cpy().scl(zScalePercent);
	}
	
}
