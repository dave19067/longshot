package dc.longshot.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public final class DecorationProfile {

	public Rectangle area;
	public boolean rotate;
	public float spawnRate; 
	public float scrollSpeed;
	public float minSize;
	public float maxSize;
	public float minXYRatio;
	public float maxXYRatio;
	public TextureRegion textureRegion;
	
	/**
	 * Constructor.
	 * @param area where the decorations should be contained in
	 * @param rotate if decoration should be rotated
	 * @param spawnRate average seconds between each created decoration
	 * @param scrollSpeed how fast the decorations move, units / second
	 * @param minSize minimum length/width of decorations
	 * @param maxSize maximum length/width of decorations
	 * @param minXYRatio minimum ratio for width to height of decorations
	 * @param maxXYRatio maximum ratio for width to height of decorations
	 * @param textureRegion texture region of decoration
	 */
	public DecorationProfile(Rectangle area, boolean rotate, float spawnRate, float scrollSpeed, float minSize, 
			float maxSize, TextureRegion textureRegion) {
		this(area, rotate, spawnRate, scrollSpeed, minSize, maxSize, 1, 1, textureRegion);
	}
	
	/**
	 * Constructor.
	 * @param area where the decorations should be contained in
	 * @param rotate if decoration should be rotated
	 * @param spawnRate average seconds between each created decoration
	 * @param scrollSpeed how fast the decorations move, units / second
	 * @param minSize minimum length/width of decorations
	 * @param maxSize maximum length/width of decorations
	 * @param minXYRatio minimum ratio for width to height of decorations
	 * @param maxXYRatio maximum ratio for width to height of decorations
	 * @param textureRegion texture region of decoration
	 */
	public DecorationProfile(Rectangle area, boolean rotate, float spawnRate, float scrollSpeed, float minSize, 
			float maxSize, float minXYRatio, float maxXYRatio, TextureRegion textureRegion) {
		this.area = area;
		this.spawnRate = spawnRate;
		this.scrollSpeed = scrollSpeed;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.minXYRatio = minXYRatio;
		this.maxXYRatio = maxXYRatio;
		this.textureRegion = textureRegion;
	}
	
}
