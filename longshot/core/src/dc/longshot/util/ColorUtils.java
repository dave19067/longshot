package dc.longshot.util;

import com.badlogic.gdx.graphics.Color;

public final class ColorUtils {

	private static final float rgbMax = 255;
	
	public static final Color toGdxColor(float r, float g, float b) {
		return new Color(r / rgbMax, g / rgbMax, b / rgbMax, 1);
	}
	
}
