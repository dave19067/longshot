package dc.longshot.models;

import com.badlogic.gdx.graphics.Color;

public final class RectangleGradient {

	private final Color topLeftColor;
	private final Color topRightColor;
	private final Color bottomLeftColor;
	private final Color bottomRightColor;
	
	public RectangleGradient(final Color topLeftColor, final Color topRightColor, final Color bottomLeftColor, 
			final Color bottomRightColor) {
		this.topLeftColor = topLeftColor;
		this.topRightColor = topRightColor;
		this.bottomLeftColor = bottomLeftColor;
		this.bottomRightColor = bottomRightColor;
	}
	
	public final Color getTopLeftColor() {
		return topLeftColor;
	}
	
	public final Color getTopRightColor() {
		return topRightColor;
	}
	
	public final Color getBottomLeftColor() {
		return bottomLeftColor;
	}
	
	public final Color getBottomRightColor() {
		return bottomRightColor;
	}
	
}
