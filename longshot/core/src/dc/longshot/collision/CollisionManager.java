package dc.longshot.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

import dc.longshot.epf.Entity;
import dc.longshot.eventmanagement.EventManager;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.TransformPart;

public final class CollisionManager {
	
	private final EventManager eventManager;
	private final Map<Entity, List<Entity>> collidedEntities = new HashMap<Entity, List<Entity>>();
	
	public CollisionManager(final EventManager eventManager) {
		this.eventManager = eventManager;
	}
	
	public final List<Entity> getCollisions(final Entity entity) {
		List<Entity> collided = new ArrayList<Entity>();
		if (collidedEntities.containsKey(entity)) {
			collided.addAll(collidedEntities.get(entity));
		}
		return collided;
	}

	public final void checkCollisions(final List<Entity> entities) {
		clearCollisions();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity1 = entities.get(i);
			if (entity1.hasActive(CollisionTypePart.class, TransformPart.class)) {
				Polygon polygon1 = entity1.get(TransformPart.class).getPolygon();
				for (int j = i + 1; j < entities.size(); j++) {
					Entity entity2 = entities.get(j);
					if (entity2.hasActive(CollisionTypePart.class, TransformPart.class)) {
						Polygon polygon2 = entity2.get(TransformPart.class).getPolygon();
						if (Intersector.overlapConvexPolygons(polygon1, polygon2)) {
							addCollision(entity1, entity2);
							addCollision(entity2, entity1);
							eventManager.notify(new CollidedEvent(entity1, entity2));
						}
					}
				}
			}
		}
	}
	
	private void clearCollisions() {
		for (Entry<Entity, List<Entity>> entry : collidedEntities.entrySet()) {
			entry.getValue().clear();
		}
	}
	
	private void addCollision(final Entity e1, final Entity e2) {
		if (!collidedEntities.containsKey(e1)) {
			collidedEntities.put(e1, new ArrayList<Entity>());
		}
		collidedEntities.get(e1).add(e2);
	}
	
}
