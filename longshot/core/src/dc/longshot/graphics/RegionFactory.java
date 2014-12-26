package dc.longshot.graphics;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;

import dc.longshot.geometry.PolygonFactory;

public final class RegionFactory {
	
	private static final EarClippingTriangulator triangulator = new EarClippingTriangulator();
	
	public static final PolygonRegion createPolygonRegion(final TextureRegion textureRegion) {
		float[] vertices = PolygonFactory.createRectangleVertices(textureRegion.getRegionWidth(), 
				textureRegion.getRegionHeight());
		return createPolygonRegion(textureRegion, vertices);
	}
	
	public static final PolygonRegion createPolygonRegion(final TextureRegion textureRegion, final float[] vertices) {
		short[] triangles = triangulator.computeTriangles(vertices).toArray();
		return new PolygonRegion(textureRegion, vertices, triangles);
	}

}
