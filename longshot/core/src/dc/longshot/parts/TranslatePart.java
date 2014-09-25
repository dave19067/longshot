package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.util.VectorUtils;

public class TranslatePart extends Part {

	private Vector2 velocity = new Vector2();
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 offset) {
		velocity = VectorUtils.getLengthened(offset, getEntity().get(SpeedPart.class).getSpeed());
		getEntity().get(TransformPart.class).setRotation(velocity.angle());
	}
	
	@Override
	public void update(float delta) {
		TransformPart transformPart = getEntity().get(TransformPart.class);
		Vector2 offset = velocity.cpy().scl(delta);
		Vector2 newPosition = transformPart.getPosition().add(offset);
		transformPart.setPosition(newPosition);
	}
	
}
