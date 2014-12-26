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
		Vector2 offset = offset(from, to);
		return MathUtils.atan2(offset.y, offset.x);
	}
	
	public static final Vector2 createVectorFromAngle(final float degrees) {
		return new Vector2(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
	}
	
	public static float relativeMiddle(final float pivotMiddle, final float objectLength) {
		return pivotMiddle - objectLength / 2;
	}
	
}
