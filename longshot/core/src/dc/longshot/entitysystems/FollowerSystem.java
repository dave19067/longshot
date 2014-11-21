package dc.longshot.entitysystems;

import java.util.ArrayList;
import java.util.List;

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
				List<Vector2> waypoints = new ArrayList<Vector2>();
				waypoints.add(entity.get(TransformPart.class).getGlobalCenter());
				follower.get(WaypointsPart.class).addWaypoints(waypoints);
			}
		}
	}

}
