package dc.longshot.game;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TranslatePart;

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
	
}
