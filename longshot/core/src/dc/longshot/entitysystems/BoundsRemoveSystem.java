package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.Bound;
import dc.longshot.parts.BoundsRemovePart;
import dc.longshot.parts.TransformPart;

public final class BoundsRemoveSystem implements EntitySystem {

	private final Rectangle boundsBox;
	private final EntityManager entityManager;
	
	public BoundsRemoveSystem(final Rectangle boundsBox, final EntityManager entityManager) {
		this.boundsBox = boundsBox;
		this.entityManager = entityManager;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(BoundsRemovePart.class)) {
			BoundsRemovePart dieOutOfBoundsPart = entity.get(BoundsRemovePart.class);
			Rectangle collisionBox = entity.get(TransformPart.class).getBoundingBox();
			Rectangle adjustedBoundsBox;
			if (dieOutOfBoundsPart.isPartial()) {
				adjustedBoundsBox = boundsBox;
			}
			else {
				adjustedBoundsBox = new Rectangle(boundsBox.x - collisionBox.width, boundsBox.y - collisionBox.height, 
					boundsBox.width + collisionBox.width * 2, boundsBox.height + collisionBox.height * 2);
			}
			if (Bound.isOutOfBounds(collisionBox, adjustedBoundsBox, dieOutOfBoundsPart.getBounds())) {
				entityManager.remove(entity);
			}
		}
	}

}
