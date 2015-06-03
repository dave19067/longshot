package dc.longshot.util;

import com.badlogic.gdx.graphics.Color;

public final class ColorUtils {

	public static final float RGB_MAX = 255;
	public static final float H_MAX = 360;
	public static final float S_MAX = 100;
	public static final float V_MAX = 100;
	
	private ColorUtils() {
	}
	
	public static final Color rgbToColor(final float r, final float g, final float b) {
		return rgbToColor(r, g, b, 1);
	}
	
	public static final Color rgbToColor(final float r, final float g, final float b, final float a) {
		return new Color(r / RGB_MAX, g / RGB_MAX, b / RGB_MAX, a);
	}
	
	/**
	 * Converts HSV color value to RGB color.
	 * @return libgdx color 
	 */
	public static final Color hsvToColor(final float h, final float s, final float v) {
		Color color;
		float newH = h / 60;
		float newS = s / S_MAX;
		float newV = v / V_MAX;
		int i = (int)Math.floor(newH);
		float f = newH - i;
		float p = newV * (1 - newS);
		float q = newV * (1 - newS * f);
		float t = newV * (1 - newS * (1 - f));
		float a = 1;
		switch (i) {
		case 0:
			color = new Color(newV, t, p, a);
			break;
		case 1:
			color = new Color(q, newV, p, a);
			break;
		case 2:
			color = new Color(p, newV, t, a);
			break;
		case 3:
			color = new Color(p, q, newV, a);
			break;
		case 4:
			color = new Color(t, p, newV, a);
			break;
		default:
			color = new Color(newV, p, q, a);
			break;
		}
		return color;
	}
	
}
