package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;

public final class WaypointsSystem implements EntitySystem {

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(WaypointsPart.class, TransformPart.class,
				SpeedPart.class)) {
			WaypointsPart waypointsPart = entity.get(WaypointsPart.class);
			float maxDistanceCovered = entity.get(SpeedPart.class).getSpeed() * delta;
			while (waypointsPart.hasWaypoints() && maxDistanceCovered > 0) {
				maxDistanceCovered = moveToWaypoint(entity, maxDistanceCovered);
			}
		}
	}

	private float moveToWaypoint(final Entity entity, final float maxDistanceCovered) {
		WaypointsPart waypointsPart = entity.get(WaypointsPart.class);
		Vector2 currentWaypoint = waypointsPart.getCurrentWaypoint();
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 oldPosition = transformPart.getPosition();
		Vector2 waypointOffset = currentWaypoint.cpy().sub(oldPosition);
		Vector2 lengthenedWaypointOffset = VectorUtils.lengthened(waypointOffset, maxDistanceCovered);
		if (lengthenedWaypointOffset.len() < waypointOffset.len()) {
			Vector2 newPosition = oldPosition.cpy().add(lengthenedWaypointOffset);
			transformPart.setPosition(newPosition);
		} else {
			// reached waypoint
			transformPart.setPosition(currentWaypoint);
			waypointsPart.removeCurrentWaypoint();
		}
		return maxDistanceCovered - waypointOffset.len();
	}

}
