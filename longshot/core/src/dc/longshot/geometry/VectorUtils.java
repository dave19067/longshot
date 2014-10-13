package dc.longshot.geometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class VectorUtils {
	
	public final static Vector2 getLengthened(final Vector2 vector, final float length) {
		Vector2 newVector = unit(vector);
		newVector.scl(length);
		return newVector;
	}
	
	public final static Vector2 unit(final Vector2 vector) {
		float length = vector.len();
		if(length == 0)
		{
			throw new IllegalArgumentException("Cannot calculate unit of vector with length 0");
		}
		Vector2 unit = vector.cpy().scl(1 / length);
		return unit;
	}

	public final static float angleToPoint(final Vector2 from, final Vector2 to) {
		Vector2 offset = to.cpy().sub(from);
		float angle = MathUtils.atan2(offset.y, offset.x);
		return angle;
	}
	
	public final static Vector2 getVectorFromAngle(final float degrees) {
		Vector2 vector = new Vector2(MathUtils.cosDeg(degrees), MathUtils.sinDeg(degrees));
		return vector;
	}
	
	public final static Vector2 center(final Vector2 position, final Vector2 size) {
		Vector2 center = new Vector2(position.x + size.x / 2, position.y + size.y / 2);
		return center;
	}
	
	public final static Vector2 relativeCenter(final Vector2 pivotCenter, final Vector2 objectSize) {
		Vector2 relativeCenter = pivotCenter.cpy().sub(objectSize.cpy().scl(0.5f));
		return relativeCenter;
	}
	
	public final static Vector2 relativeEdgeMiddle(final Vector2 edgeStart, final Vector2 edgeEnd, 
			final float objectLength) {
		Vector2 difference = edgeEnd.cpy().sub(edgeStart);
		Vector2 offset = getLengthened(difference, (difference.len() - objectLength) / 2);
		return edgeStart.cpy().add(offset);
	}
	
	public static float relativeMiddle(float pivotMiddle, float objectLength) {
		float relativeMiddle = pivotMiddle - objectLength / 2;
		return relativeMiddle;
	}
	
}
