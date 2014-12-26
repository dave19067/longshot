package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.LinearUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;

public final class WaypointsSystem implements EntitySystem {

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(WaypointsPart.class)) {
			WaypointsPart waypointsPart = entity.get(WaypointsPart.class);
			float endDistance = waypointsPart.getPathDistance() - waypointsPart.getEndBuffer();
			float maxDistanceCovered;
			if (endDistance > 0) {
				maxDistanceCovered = LinearUtils.distance(entity.get(SpeedPart.class).getSpeed(), delta);
			}
			else {
				maxDistanceCovered = 0;
			}
			while (waypointsPart.hasWaypoints() && maxDistanceCovered > VectorUtils.BUFFER) {
				maxDistanceCovered = moveToWaypoint(entity, maxDistanceCovered);
			}
		}
	}

	private float moveToWaypoint(final Entity entity, float maxDistanceCovered) {
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
