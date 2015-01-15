package dc.longshot.game;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.math.Rectangle;

public final class DecorationProfile {

	public Rectangle area;
	public boolean rotate;
	public float spawnRate; 
	public float minSize;
	public float maxSize;
	public float minXYRatio;
	public float maxXYRatio;
	public float minZ;
	public float maxZ;
	public float minSpeed;
	public float maxSpeed;
	public PolygonRegion region;
	
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
	public DecorationProfile(final Rectangle area, final boolean rotate, final float spawnRate, 
			final float minSize, final float maxSize, final float minZ, final float maxZ, 
			final float minSpeed, final float maxSpeed, final PolygonRegion region) {
		this(area, rotate, spawnRate, minSize, maxSize, 1, 1, minZ, maxZ, minSpeed, maxSpeed, region);
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
	public DecorationProfile(final Rectangle area, final boolean rotate, final float spawnRate, 
			final float minSize, final float maxSize, final float minXYRatio, 
			final float maxXYRatio, final float minZ, final float maxZ, final float minSpeed, final float maxSpeed, 
			final PolygonRegion region) {
		this.area = area;
		this.spawnRate = spawnRate;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.minXYRatio = minXYRatio;
		this.maxXYRatio = maxXYRatio;
		this.minZ = minZ;
		this.maxZ = maxZ;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		this.region = region;
	}
	
}
