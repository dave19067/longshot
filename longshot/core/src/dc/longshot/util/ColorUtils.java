package dc.longshot.util;

import com.badlogic.gdx.graphics.Color;

public final class ColorUtils {

	private static final float rgbMax = 255;
	
	public static final Color toGdxColor(final float r, final float g, final float b) {
		return toGdxColor(r, g, b, 1);
	}
	
	public static final Color toGdxColor(final float r, final float g, final float b, final float a) {
		return new Color(r / rgbMax, g / rgbMax, b / rgbMax, a / rgbMax);
	}
	
}
