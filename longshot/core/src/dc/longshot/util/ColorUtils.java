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
		return rgbToColor(r, g, b, RGB_MAX);
	}
	
	public static final Color rgbToColor(final float r, final float g, final float b, final float a) {
		return new Color(r / RGB_MAX, g / RGB_MAX, b / RGB_MAX, a / RGB_MAX);
	}
	
	/**
	 * TODO: Cleanup
	 * Converts HSV color sytem to RGB
	 * @return RGB values in Libgdx Color class */
	public static final Color hsvToColor(final float h, final float s, final float v) {
		int r, g, b;
		int i;
		float f, p, q, t;
		float newH = (float)Math.max(0.0, Math.min(H_MAX, h));
		float newS = (float)Math.max(0.0, Math.min(S_MAX, s));
		float newV = (float)Math.max(0.0, Math.min(V_MAX, v));
		newS /= 100;
		newV /= 100;
		newH /= 60;
		i = (int)Math.floor(newH);
		f = newH - i;
		p = newV * (1 - newS);
		q = newV * (1 - newS * f);
		t = newV * (1 - newS * (1 - f));
		switch (i) {
		case 0:
			r = Math.round(RGB_MAX * newV);
			g = Math.round(RGB_MAX * t);
			b = Math.round(RGB_MAX * p);
			break;
		case 1:
			r = Math.round(RGB_MAX * q);
			g = Math.round(RGB_MAX * newV);
			b = Math.round(RGB_MAX * p);
			break;
		case 2:
			r = Math.round(RGB_MAX * p);
			g = Math.round(RGB_MAX * newV);
			b = Math.round(RGB_MAX * t);
			break;
		case 3:
			r = Math.round(RGB_MAX * p);
			g = Math.round(RGB_MAX * q);
			b = Math.round(RGB_MAX * newV);
			break;
		case 4:
			r = Math.round(RGB_MAX * t);
			g = Math.round(RGB_MAX * p);
			b = Math.round(RGB_MAX * newV);
			break;
		default:
			r = Math.round(RGB_MAX * newV);
			g = Math.round(RGB_MAX * p);
			b = Math.round(RGB_MAX * q);
		}

		return new Color(r / RGB_MAX, g / RGB_MAX, b / RGB_MAX, 1);
	}
	
}
