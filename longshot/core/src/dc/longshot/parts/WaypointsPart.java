package dc.longshot.parts;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public final class WaypointsPart extends Part {

	private List<Vector2> waypoints = new ArrayList<Vector2>();

	public final boolean hasWaypoints() {
		return waypoints.size() > 0;
	}
	
	public final Vector2 getCurrentWaypoint() {
		if (!hasWaypoints()) {
			throw new UnsupportedOperationException("Could not get current waypoint because there are no waypoints");
		}
		return waypoints.get(0);
	}
	
	public final void removeCurrentWaypoint() {
		if (!hasWaypoints()) {
			throw new UnsupportedOperationException("Could not remove current waypoint because there are no waypoints");
		}
		waypoints.remove(0);
	}
	
	public final void setWaypoints(final List<Vector2> waypoints) {
		this.waypoints = waypoints;
	}
	
}
