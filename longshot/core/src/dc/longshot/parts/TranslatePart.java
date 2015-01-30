package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

public class TranslatePart {

	private Vector2 velocity = new Vector2(0, 0);
	
	public TranslatePart() {
	}
	
	public final Vector2 getVelocity() {
		return new Vector2(velocity);
	}
	
	public final void setVelocity(final Vector2 velocity) {
		this.velocity = velocity;
	}
	
}
