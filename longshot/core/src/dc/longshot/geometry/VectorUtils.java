package dc.longshot.geometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class VectorUtils {
	
	public final static Vector2 unit(final Vector2 vector) {
		float length = vector.len();
		return vector.cpy().scl(1 / length);
	}
	
	public final static Vector2 lengthened(final Vector2 vector, final float length) {
		return vector.cpy().scl(length / vector.len());
	}

	public final static float angleToPoint(final Vector2 from, final Vector2 to) {
		Vector2 offset = to.cpy().sub(from);
		return MathUtils.atan2(offset.y, offset.x);
	}
	
	public final static Vector2 createVectorFromAngle(final float degrees) {
		return new Vector2(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
	}

	public final static Vector2 relativeEdgeMiddle(final Vector2 edgeStart, final Vector2 edgeEnd, 
			final float objectLength) {
		Vector2 edgeDifference = edgeEnd.cpy().sub(edgeStart);
		Vector2 offset = lengthened(edgeDifference, (edgeDifference.len() - objectLength) / 2);
		return edgeStart.cpy().add(offset);
	}
	
	public static float relativeMiddle(float pivotMiddle, float objectLength) {
		return pivotMiddle - objectLength / 2;
	}
	
}
