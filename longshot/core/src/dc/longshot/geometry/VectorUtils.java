package dc.longshot.geometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class VectorUtils {
	
	/**
	 * Buffer used for floating point calculations involving vectors.
	 * Vector calculations don't always round well to exact numbers, so this buffer can be used to check if any 
	 * attribute of the vector (e.g. x, y, length) is near the expected number.
	 */
	public static final float BUFFER = 1e-5f;
	
	private VectorUtils() {
	}
	
	public static final Vector2 unit(final Vector2 vector) {
		float length = vector.len();
		return vector.cpy().scl(1 / length);
	}
	
	public static final Vector2 lengthened(final Vector2 vector, final float length) {
		return vector.cpy().scl(length / vector.len());
	}
	
	public static final Vector2 offset(final Vector2 from, final Vector2 to) {
		return to.cpy().sub(from);
	}

	public static final float angleToPoint(final Vector2 from, final Vector2 to) {
		Vector2 offset = to.cpy().sub(from);
		return MathUtils.atan2(offset.y, offset.x);
	}
	
	public static final Vector2 createVectorFromAngle(final float degrees) {
		return new Vector2(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
	}

	public static final Vector2 relativeEdgeMiddle(final Vector2 edgeStart, final Vector2 edgeEnd, 
			final float objectLength) {
		Vector2 edgeDifference = edgeEnd.cpy().sub(edgeStart);
		Vector2 offset = lengthened(edgeDifference, (edgeDifference.len() - objectLength) / 2);
		return edgeStart.cpy().add(offset);
	}
	
	public static float relativeMiddle(float pivotMiddle, float objectLength) {
		return pivotMiddle - objectLength / 2;
	}
	
}
