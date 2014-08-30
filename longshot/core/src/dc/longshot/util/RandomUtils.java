package dc.longshot.util;

public final class RandomUtils {

	public static final float nextFloat(float min, float max) {
		return min + (float)(Math.random() * (max - min)); 
	}
	
}
