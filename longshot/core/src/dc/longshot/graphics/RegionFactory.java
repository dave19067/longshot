package dc.longshot.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;

import dc.longshot.geometry.PolygonFactory;

public final class RegionFactory {
	
	private static final EarClippingTriangulator triangulator = new EarClippingTriangulator();
	
	public static final PolygonRegion createPolygonRegion(final TextureRegion textureRegion, final int x, final int y,
			final int width, final int height) {
		// TODO: Duplicate code with below
		float[] vertices = PolygonFactory.createRectangleVertices(x, y, width, height);
		short[] triangles = triangulator.computeTriangles(vertices).toArray();
		return new PolygonRegion(textureRegion, vertices, triangles);
	}
	
	public static final PolygonRegion createPolygonRegion(final Texture texture) {
		float[] vertices = PolygonFactory.createRectangleVertices(texture.getWidth(), texture.getHeight());
		return createPolygonRegion(texture, vertices);
	}
	
	public static final PolygonRegion createPolygonRegion(final Texture texture, final float[] vertices) {
		short[] triangles = triangulator.computeTriangles(vertices).toArray();
		return new PolygonRegion(new TextureRegion(texture), vertices, triangles);
	}

}
