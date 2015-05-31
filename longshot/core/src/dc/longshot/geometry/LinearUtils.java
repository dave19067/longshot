package dc.longshot.geometry;

public final class LinearUtils {

	private LinearUtils() {
	}
	
	public static final float distance(final float rate, final float elapsedTime) {
		return rate * elapsedTime;
	}

	public static float relativeMiddle(final int pivotLength, final int objectLength) {
		return pivotLength / 2f - objectLength / 2f;
	}
	
}
