package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.FollowerPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;

public final class FollowerSystem implements EntitySystem {

	@Override
	public void update(float delta, Entity entity) {;
		if (entity.hasActive(FollowerPart.class)) {
			for (Entity follower : entity.get(FollowerPart.class).getFollowers()) {
				Vector2 waypoint = entity.get(TransformPart.class).getGlobalCenter();
				follower.get(WaypointsPart.class).addWaypoint(waypoint);
			}
		}
	}

}
