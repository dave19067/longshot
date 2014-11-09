package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.TransformPart;

public final class BoundPositionSystem implements EntitySystem {
	
	private final Rectangle boundsBox;

	public BoundPositionSystem(final Rectangle boundsBox) {
		this.boundsBox = boundsBox;
	}
	
	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.hasActive(TransformPart.class, BoundsPart.class)) {
			TransformPart transformPart = entity.get(TransformPart.class);
			Rectangle boundingBox = transformPart.getBoundingBox();
			List<Bound> bounds = Bound.getViolatedBounds(boundingBox, boundsBox);
			Vector2 newPosition = transformPart.getPosition();
			
			for (Bound checkedBound : entity.get(BoundsPart.class).getBounds()) {
				if (bounds.contains(checkedBound)) {
					switch (checkedBound) {
					case LEFT:
						newPosition.x -= (boundingBox.x - VectorUtils.BUFFER);
						break;
					case RIGHT:
						newPosition.x -= (PolygonUtils.right(boundingBox) - PolygonUtils.right(boundsBox));
						break;
					case TOP:
						newPosition.y -= (PolygonUtils.top(boundingBox) - PolygonUtils.top(boundsBox));
						break;
					case BOTTOM:
						newPosition.y -= (boundingBox.y - VectorUtils.BUFFER);
						break;
					}
				}
			}
			
			entity.get(TransformPart.class).setPosition(newPosition);
		}
	}

}
