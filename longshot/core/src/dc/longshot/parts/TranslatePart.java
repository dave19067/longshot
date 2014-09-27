package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.util.VectorUtils;

public class TranslatePart extends Part {

	private boolean autoRotate;
	private Vector2 velocity = new Vector2(0, 0);
	
	public TranslatePart(boolean autoRotate) {
		this.autoRotate = autoRotate;
	}
	
	public Vector2 getVelocity() {
		return new Vector2(velocity);
	}
	
	public void setVelocity(Vector2 direction) {
		if (direction.equals(new Vector2(0, 0))) {
			velocity = new Vector2(0, 0);
		}
		else {
			velocity = VectorUtils.getLengthened(direction, getEntity().get(SpeedPart.class).getSpeed());
			if (autoRotate) {
				getEntity().get(TransformPart.class).setRotation(velocity.angle());
			}
		}
	}
	
	@Override
	public void update(float delta) {
		TransformPart transformPart = getEntity().get(TransformPart.class);
		Vector2 offset = velocity.cpy().scl(delta);
		Vector2 newPosition = transformPart.getPosition().add(offset);
		transformPart.setPosition(newPosition);
	}
	
}
