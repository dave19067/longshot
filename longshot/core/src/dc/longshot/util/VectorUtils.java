package dc.longshot.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VectorUtils {
	
	public static Vector2 getLengthened(Vector2 vector, float length) {
		Vector2 newVector = unit(vector);
		newVector.scl(length);
		return newVector;
	}
	
	public static Vector2 unit(Vector2 vector) {
		float length = vector.len();
		if(length == 0)
		{
			throw new IllegalArgumentException("Cannot calculate unit of vector with length 0");
		}
		Vector2 unit = vector.cpy().scl(1 / length);
		return unit;
	}

	public static float angleToPoint(Vector2 from, Vector2 to) {
		Vector2 offset = to.cpy().sub(from);
		float angle = MathUtils.atan2(offset.y, offset.x);
		return angle;
	}
	
	public static Vector2 center(Vector2 position, Vector2 size) {
		Vector2 center = new Vector2(position.x + size.x / 2, position.y + size.y / 2);
		return center;
	}
	
	public static Vector2 relativeCenter(Vector2 pivotCenter, Vector2 objectSize) {
		Vector2 relativeCenter = pivotCenter.cpy().sub(objectSize.cpy().scl(0.5f));
		return relativeCenter;
	}
	
	public static float relativeMiddle(float pivotMiddle, float objectLength) {
		float relativeMiddle = pivotMiddle - objectLength / 2;
		return relativeMiddle;
	}
	
}
