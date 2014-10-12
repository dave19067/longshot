package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.models.Bound;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.util.BoundUtils;

public class BoundPositionSystem implements EntitySystem {

	private Rectangle boundsBox;

	public BoundPositionSystem(Rectangle boundsBox) {
		this.boundsBox = boundsBox;
	}
	
	@Override
	public void update(float delta, Entity entity) {
		// Restrict entity in bounds
		if (entity.has(TransformPart.class) && entity.has(BoundsPart.class)) {
			List<Bound> bounds = BoundUtils.checkOutOfBounds(entity.get(TransformPart.class).getBoundingBox(), 
					boundsBox);
			TransformPart transformPart = entity.get(TransformPart.class);
			Rectangle boundingBox = transformPart.getBoundingBox();
			Vector2 newPosition = transformPart.getPosition();
			// Buffer required for floating point calculations to make sure object is completely in bounds
			float buffer = 0.00001f;
			
			for (Bound checkedBound : entity.get(BoundsPart.class).getBounds()) {
				if (bounds.contains(checkedBound)) {
					switch (checkedBound) {
					case LEFT:
						newPosition.x -= (boundingBox.x - buffer);
						break;
					case RIGHT:
						newPosition.x -= (boundingBox.x + boundingBox.width - (boundsBox.x + boundsBox.width));
						break;
					case TOP:
						newPosition.y -= (boundingBox.y + boundingBox.height - (boundsBox.y + boundsBox.height));
						break;
					case BOTTOM:
						newPosition.y -= (boundingBox.y - buffer);
						break;
					}
				}
			}
			
			entity.get(TransformPart.class).setPosition(newPosition);
		}
	}

}
