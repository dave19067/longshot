package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.models.Bound;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.ShotStatsPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.util.BoundUtils;

public class BounceSystem implements EntitySystem {
	
	private Rectangle boundsBox;
	
	public BounceSystem(Rectangle boundsBox) {
		this.boundsBox = boundsBox;
	}
	
	@Override
	public void update(float dt, Entity entity) {
		// Bounce entity off walls
		if (entity.has(TransformPart.class) && entity.has(TranslatePart.class) && entity.has(BouncePart.class)) {
			List<Bound> bounds = BoundUtils.checkOutOfBounds(entity.get(TransformPart.class).getBoundingBox(), 
					boundsBox);
			Vector2 velocity = entity.get(TranslatePart.class).getVelocity();
			Vector2 newVelocity = velocity.cpy();
			
			for (Bound checkedBound : entity.get(BouncePart.class).getBounds()) {
				if (bounds.contains(checkedBound)) {
					switch (checkedBound) {
					case LEFT:
						if (velocity.x < 0) {
							newVelocity.x *= -1;
						}
						break;
					case RIGHT:
						if (velocity.x > 0) {
							newVelocity.x *= -1;
						}
						break;
					case TOP:
						if (velocity.y > 0) {
							newVelocity.y *= -1;
						}
						break;
					case BOTTOM:
						if (velocity.y < 0) {
							newVelocity.y *= -1;
						}
						break;
					}
				}
			}
			
			entity.get(TranslatePart.class).setVelocity(newVelocity);
			
			// Increase bounce stat
			if (entity.has(ShotStatsPart.class)) {
				if (bounds.contains(Bound.RIGHT) || bounds.contains(Bound.LEFT)) {
					ShotStatsPart shotStatsPart = entity.get(ShotStatsPart.class);
					shotStatsPart.setBounceNum(shotStatsPart.getBounceNum() + 1);
				}
			}
		}
	}
	
}
