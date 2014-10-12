package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.util.BoundUtils;

public class OutOfBoundsRemoveSystem implements EntitySystem {

	private Rectangle boundsBox;
	private EntityManager entityManager;
	
	public OutOfBoundsRemoveSystem(Rectangle boundsBox, EntityManager entityManager) {
		this.boundsBox = boundsBox;
		this.entityManager = entityManager;
	}
	
	@Override
	public void update(float delta, Entity entity) {
		// Remove if out of bounds
		if (entity.has(BoundsDiePart.class)) {
			if (BoundUtils.isOutOfBounds(entity.get(TransformPart.class).getBoundingBox(), boundsBox,  
					entity.get(BoundsDiePart.class).getBounds())) {
				entityManager.remove(entity);
			}
		}
	}

}
