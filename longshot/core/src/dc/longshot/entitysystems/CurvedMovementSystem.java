package dc.longshot.entitysystems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.LinearUtils;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.level.LevelUtils;
import dc.longshot.parts.CurvedMovementPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;

public final class CurvedMovementSystem extends EntitySystem {
	
	private final Rectangle boundsBox;
	
	public CurvedMovementSystem(final Rectangle boundsBox) {
		this.boundsBox = boundsBox;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(CurvedMovementPart.class)) {
			WaypointsPart waypointsPart = entity.get(WaypointsPart.class);
			TransformPart transformPart = entity.get(TransformPart.class);
			float maxDistanceCovered = LinearUtils.distance(entity.get(SpeedPart.class).getSpeed(), delta);
			List<Vector2> waypoints = waypointsPart.getWaypoints();
			if (waypoints.isEmpty()) {
				setCurveWaypoints(entity, transformPart.getCenter());
			}
			else if (maxDistanceCovered >= LevelUtils.getPathDistance(entity)) {
				Vector2 lastWaypoint = waypoints.get(waypoints.size() - 1);
				setCurveWaypoints(entity, lastWaypoint);
			}
		}
	}
	
	private void setCurveWaypoints(final Entity entity, final Vector2 start) {
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 end = createEndPoint(start);
		float curveSize = entity.get(CurvedMovementPart.class).getCurveSize();
		Vector2 startCurve = createStartCurvePoint(start, transformPart.getRotation(), curveSize);
		Vector2 endCurve = createEndCurvePoint(start, end, curveSize);
		List<Vector2> waypoints = createWaypoints(start, startCurve, endCurve, end);
		entity.get(WaypointsPart.class).addWaypoints(waypoints);
	}
	
	private List<Vector2> createWaypoints(final Vector2 p0, final Vector2 p1, final Vector2 p2, final Vector2 p3) {
		final int sampleNum = 20;
		List<Vector2> waypoints = new ArrayList<Vector2>();
		for (int i = 0; i < sampleNum; i++) {
			Vector2 out = new Vector2();
			Bezier.cubic(out, (float)i / sampleNum, p0, p1, p2, p3, new Vector2());
			waypoints.add(out);
		}
		return waypoints;
	}
	
	private Vector2 createStartCurvePoint(final Vector2 start, final float currentRotation, final float curveSize) {
		Vector2 curvePointOffset = VectorUtils.fromAngle(-currentRotation, curveSize);
		return start.cpy().add(curvePointOffset);
	}
	
	private Vector2 createEndCurvePoint(final Vector2 start, final Vector2 end, final float curveSize) {
		Vector2 offset = VectorUtils.offset(end, start);
		Vector2 curvePointOffset = offset.rotate(MathUtils.random(-90, 90));
		curvePointOffset = VectorUtils.lengthened(curvePointOffset, MathUtils.random(0, curveSize));
		return end.cpy().add(curvePointOffset);
	}
	
	private Vector2 createEndPoint(final Vector2 start) {
		float maxOffsetX = 20;
		float minX = Math.max(start.x - maxOffsetX / 2, boundsBox.x);
		float maxX = Math.min(start.x + maxOffsetX / 2, PolygonUtils.right(boundsBox));
		float endX = MathUtils.random(minX, maxX);
		float maxOffsetY = 5;
		float endY = MathUtils.random(start.y - maxOffsetY, start.y);
		return new Vector2(endX, endY);
	}

}
