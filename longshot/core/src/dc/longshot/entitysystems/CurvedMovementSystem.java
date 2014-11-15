package dc.longshot.entitysystems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.CurvedMovementPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;

// TODO: Refine
public final class CurvedMovementSystem implements EntitySystem {
	
	private final int SAMPLE_NUM = 20;
	
	private final Rectangle boundsBox;
	
	public CurvedMovementSystem(final Rectangle boundsBox) {
		this.boundsBox = boundsBox;
	}

	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.hasActive(CurvedMovementPart.class)) {
			if (!entity.get(WaypointsPart.class).hasWaypoints()) {
				setCurveWaypoints(entity);
			}
		}
	}
	
	private void setCurveWaypoints(final Entity entity) {
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 start = transformPart.getGlobalCenter();
		Vector2 end = createEndPoint(start);
		float curveSize = entity.get(CurvedMovementPart.class).getCurveSize();
		Vector2 startCurve = createStartCurvePoint(start, transformPart.getRotation(), curveSize);
		Vector2 endCurve = createEndCurvePoint(start, end, curveSize);
		List<Vector2> waypoints = createWaypoints(start, startCurve, endCurve, end);
		entity.get(WaypointsPart.class).addWaypoints(waypoints);
	}
	
	private List<Vector2> createWaypoints(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3) {
		List<Vector2> waypoints = new ArrayList<Vector2>();
		for (int i = 0; i < SAMPLE_NUM; i++) {
			Vector2 out = new Vector2();
			Bezier.cubic(out, (float)i / SAMPLE_NUM, p0, p1, p2, p3, new Vector2());
			waypoints.add(out);
		}
		return waypoints;
	}
	
	private Vector2 createStartCurvePoint(final Vector2 start, final float currentRotation, final float curveSize) {
		Vector2 curvePointOffset = VectorUtils.createVectorFromAngle(-currentRotation);
		curvePointOffset = VectorUtils.lengthened(curvePointOffset, curveSize);
		return start.cpy().add(curvePointOffset);
	}
	
	private Vector2 createEndCurvePoint(final Vector2 start, final Vector2 end, final float curveSize) {
		Vector2 offset = start.cpy().sub(end);
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
