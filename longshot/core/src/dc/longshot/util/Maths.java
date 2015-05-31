package dc.longshot.util;

public final class Maths {

	private Maths() {
	}
	
	public static int round(final int value, final int intervalLength) {
		return value - value % intervalLength;
	}
	
}
