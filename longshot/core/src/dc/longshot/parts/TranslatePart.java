package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.VectorUtils;

public class TranslatePart extends Part {

	private Vector2 velocity = new Vector2(0, 0);
	
	public TranslatePart() {
	}
	
	public final Vector2 getVelocity() {
		return new Vector2(velocity);
	}
	
	public final void setVelocity(final Vector2 velocity) {
		this.velocity = velocity;
	}
	
	// TODO: this method is too high-level to be in this class
	public final void setDirection(final Vector2 direction) {
		if (direction.equals(new Vector2(0, 0))) {
			velocity = new Vector2(0, 0);
		}
		else {
			velocity = VectorUtils.lengthened(direction, entity.get(SpeedPart.class).getSpeed());
		}
	}
	
}
