package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.VectorUtils;

public class TranslatePart extends Part {

	private final boolean autoRotate;
	private Vector2 velocity = new Vector2(0, 0);
	
	public TranslatePart(final boolean autoRotate) {
		this.autoRotate = autoRotate;
	}
	
	public final Vector2 getVelocity() {
		return new Vector2(velocity);
	}
	
	public final void setVelocity(final Vector2 direction) {
		if (direction.equals(new Vector2(0, 0))) {
			velocity = new Vector2(0, 0);
		}
		else {
			velocity = VectorUtils.getLengthened(direction, entity.get(SpeedPart.class).getSpeed());
			if (autoRotate) {
				entity.get(TransformPart.class).setRotation(velocity.angle());
			}
		}
	}
	
	@Override
	public final void update(final float delta) {
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 offset = velocity.cpy().scl(delta);
		Vector2 newPosition = transformPart.getPosition().add(offset);
		transformPart.setPosition(newPosition);
	}
	
}
