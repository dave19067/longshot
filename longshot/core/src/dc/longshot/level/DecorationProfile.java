package dc.longshot.level;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.math.Rectangle;

import dc.longshot.util.FloatRange;

public final class DecorationProfile {

	public Rectangle area;
	public boolean rotate;
	public float spawnRate; 
	public FloatRange sizeRange;
	public FloatRange xyRatioRange;
	public FloatRange zRange;
	public float minZScale;
	public FloatRange speedRange;
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
			final FloatRange sizeRange, final FloatRange zRange, final float minZScale, 
			final FloatRange speedRange, final PolygonRegion region) {
		this(area, rotate, spawnRate, sizeRange, new FloatRange(1, 1), zRange, minZScale, speedRange, region);
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
			final FloatRange sizeRange, final FloatRange xyRatioRange, final FloatRange zRange, 
			final float minZScale, final FloatRange speedRange, final PolygonRegion region) {
		this.area = area;
		this.spawnRate = spawnRate;
		this.sizeRange = sizeRange;
		this.xyRatioRange = xyRatioRange;
		this.zRange = zRange;
		this.minZScale = minZScale;
		this.speedRange = speedRange;
		this.region = region;
	}
	
}
