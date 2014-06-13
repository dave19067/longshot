package dc.longshot.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VectorUtils {
	
	public static Vector2 lengthen(Vector2 vector, float length) {
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
	
}
