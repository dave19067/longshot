package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.ConvexHullCache;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.TransformPart;

public final class TransformSystem extends EntitySystem {
	
	private final ConvexHullCache convexHullCache;
	
	public TransformSystem(final ConvexHullCache convexHullCache) {
		this.convexHullCache = convexHullCache;
	}
	
	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(TransformPart.class)) {
			TransformPart transformPart = entity.get(TransformPart.class);
			DrawablePart drawablePart = entity.get(DrawablePart.class);
			// TODO: NULL CHECK BAD!
			if (drawablePart.getTextureName() != null) {
				Polygon polygon = convexHullCache.create(drawablePart.getTextureName(), transformPart.getStartingSize());
				transformPart.setVertices(polygon.getVertices());
				Vector2 startingOrigin = transformPart.getStartingOrigin();
				if (startingOrigin != null) {
					transformPart.setOrigin(startingOrigin);
				}
			}
		}
	}

}
