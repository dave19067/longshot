package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.Bound;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.ShotStatsPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public final class BounceSystem implements EntitySystem {
	
	private final Rectangle boundsBox;
	
	public BounceSystem(Rectangle boundsBox) {
		this.boundsBox = boundsBox;
	}
	
	@Override
	public final void update(final float dt, final Entity entity) {
		if (entity.hasActive(TransformPart.class, TranslatePart.class, BouncePart.class)) {
			List<Bound> bounds = Bound.getViolatedBounds(entity.get(TransformPart.class).getBoundingBox(), boundsBox);
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
			increaseBounceStat(entity, bounds);
		}
	}
	
	private void increaseBounceStat(Entity entity, List<Bound> bounds) {
		if (entity.hasActive(ShotStatsPart.class)) {
			if (bounds.contains(Bound.RIGHT) || bounds.contains(Bound.LEFT)) {
				ShotStatsPart shotStatsPart = entity.get(ShotStatsPart.class);
				shotStatsPart.setBounceNum(shotStatsPart.getBounceNum() + 1);
			}
		}
	}
	
}
