package dc.longshot.util;

import com.badlogic.gdx.math.MathUtils;

public final class FloatRange {

	private final float min;
	private final float max;
	
	public FloatRange(final float min, final float max) {
		this.min = min;
		this.max = max;
	}
	
	public final float min() {
		return min;
	}
	
	public final float max() {
		return max;
	}
	
	public final float difference() {
		return max - min;
	}
	
	public final float random() {
		return MathUtils.random(min, max);
	}
	
}
