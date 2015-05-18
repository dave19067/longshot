package dc.longshot.util;

import com.badlogic.gdx.math.MathUtils;

public final class IntRange {

	private final int min;
	private final int max;
	
	public IntRange(final int min, final int max) {
		this.min = min;
		this.max = max;
	}
	
	public final int min() {
		return min;
	}
	
	public final int max() {
		return max;
	}
	
	public final int difference() {
		return max - min;
	}
	
	public final int random() {
		return MathUtils.random(min, max);
	}

}
