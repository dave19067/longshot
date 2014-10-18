package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.Bound;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.TransformPart;

public final class OutOfBoundsRemoveSystem implements EntitySystem {

	private final Rectangle boundsBox;
	private final EntityManager entityManager;
	
	public OutOfBoundsRemoveSystem(final Rectangle boundsBox, final EntityManager entityManager) {
		this.boundsBox = boundsBox;
		this.entityManager = entityManager;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.has(BoundsDiePart.class)) {
			if (Bound.isOutOfBounds(entity.get(TransformPart.class).getBoundingBox(), boundsBox,  
					entity.get(BoundsDiePart.class).getBounds())) {
				entityManager.remove(entity);
			}
		}
	}

}
