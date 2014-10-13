package dc.longshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.eventmanagement.EventManager;
import dc.longshot.events.CollidedEvent;
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
		for (Entry<Entity, List<Entity>> entry : collidedEntities.entrySet()) {
			entry.getValue().clear();
		}
		
		for (int i = 0; i < entities.size(); i++) {
			Entity entity1 = entities.get(i);
			Rectangle boundingBox1 = entity1.get(TransformPart.class).getBoundingBox();
			
			if (entity1.isActive() && entity1.has(TransformPart.class)) {
				for (int j = i + 1; j < entities.size(); j++) {
					Entity entity2 = entities.get(j);
					Rectangle boundingBox2 = entity2.get(TransformPart.class).getBoundingBox();
					
					if (entity2.isActive() && entity2.has(TransformPart.class)) {
						if (Intersector.overlaps(boundingBox1, boundingBox2)) {
							if (!collidedEntities.containsKey(entity1)) {
								collidedEntities.put(entity1, new ArrayList<Entity>());
							}
							if (!collidedEntities.containsKey(entity2)) {
								collidedEntities.put(entity2, new ArrayList<Entity>());
							}
							collidedEntities.get(entity1).add(entity2);
							collidedEntities.get(entity2).add(entity1);
							eventManager.notify(new CollidedEvent(entity1, entity2));
						}
					}
				}
			}
		}
	}
	
}
