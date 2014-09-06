package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.util.VectorUtils;

public class TranslatePart extends Part {

	private Vector2 velocity = new Vector2(0, 0);
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 offset) {
		// TODO: move speed logic
		this.velocity = VectorUtils.lengthen(offset, getEntity().get(SpeedPart.class).getSpeed());
	}
	
	@Override
	public void update(float delta) {
		TransformPart transformPart = getEntity().get(TransformPart.class);
		Vector2 newPosition = transformPart.getPosition().add(velocity.cpy().scl(delta));
		transformPart.setPosition(newPosition);
	}
	
}
