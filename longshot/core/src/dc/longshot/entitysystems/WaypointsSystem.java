package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.LinearUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.level.LevelUtils;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;

public final class WaypointsSystem extends EntitySystem {

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(WaypointsPart.class)) {
			WaypointsPart waypointsPart = entity.get(WaypointsPart.class);
			float endDistance = LevelUtils.getPathDistance(entity) - waypointsPart.getEndBuffer();
			float maxDistanceCovered;
			if (endDistance > 0) {
				maxDistanceCovered = LinearUtils.distance(entity.get(SpeedPart.class).getSpeed(), delta);
			}
			else {
				maxDistanceCovered = 0;
			}
			List<Vector2> waypoints = waypointsPart.getWaypoints();
			while (!waypoints.isEmpty() && maxDistanceCovered > VectorUtils.BUFFER) {
				maxDistanceCovered = moveToWaypoint(entity, maxDistanceCovered);
			}
		}
	}

	private float moveToWaypoint(final Entity entity, final float maxDistanceCovered) {
		WaypointsPart waypointsPart = entity.get(WaypointsPart.class);
		Vector2 currentWaypoint = waypointsPart.getCurrentWaypoint();
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 oldGlobalCenter = transformPart.getCenter();
		Vector2 waypointOffset = VectorUtils.offset(oldGlobalCenter, currentWaypoint);
		Vector2 offset;
		if (maxDistanceCovered < waypointOffset.len()) {
			offset = VectorUtils.lengthened(waypointOffset, maxDistanceCovered);
		} else {
			// reached waypoint
			offset = waypointOffset;
			waypointsPart.removeCurrentWaypoint();
		}
		Vector2 newPosition = transformPart.getPosition().add(offset);
		transformPart.setPosition(newPosition);
		return maxDistanceCovered - offset.len();
	}

}
