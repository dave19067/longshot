package dc.longshot.parts;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public final class WaypointsPart extends Part {

	private float endBuffer = 0;
	private final List<Vector2> waypoints = new ArrayList<Vector2>();

	/**
	 * @return the minimum distance that must be kept between the center and the last waypoint
	 */
	public final float getEndBuffer() {
		return endBuffer;
	}
	
	/**
	 * Sets the end buffer.
	 * @param endBuffer the minimum distance that must be kept between the center and the last waypoint
	 */
	public final void setEndBuffer(final float endBuffer) {
		this.endBuffer = endBuffer;
	}
	
	public final Vector2 getCurrentWaypoint() {
		if (waypoints.isEmpty()) {
			throw new UnsupportedOperationException("Could not get current waypoint because there are no waypoints");
		}
		return waypoints.get(0);
	}
	
	public final void removeCurrentWaypoint() {
		if (waypoints.isEmpty()) {
			throw new UnsupportedOperationException("Could not remove current waypoint because there are no waypoints");
		}
		waypoints.remove(0);
	}
	
	public final List<Vector2> getWaypoints() {
		return new ArrayList<Vector2>(waypoints);
	}
	
	public final void addWaypoint(final Vector2 waypoint) {
		waypoints.add(waypoint);
	}
	
	public final void addWaypoints(final List<Vector2> waypoints) {
		this.waypoints.addAll(waypoints);
	}
	
}
