package dc.longshot.geometry;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.graphics.TextureCache;
import dc.longshot.graphics.TextureGeometry;

public final class ConvexHullCache {
	
	private final TextureCache textureCache;
	private final Map<String, float[]> convexHulls = new HashMap<String, float[]>();
	
	public ConvexHullCache(final TextureCache textureCache) {
		this.textureCache = textureCache;
	}
	
	public final Polygon create(final String regionName, final Vector2 size) {
		if (!convexHulls.containsKey(regionName)) {
			TextureRegion textureRegion = textureCache.getRegion(regionName);
			float[] convexHull = TextureGeometry.createConvexHull(textureRegion);
			// libgdx y-axis is flipped
			VertexUtils.flipY(convexHull);
			convexHulls.put(regionName, convexHull);
		}
		float[] vertices = sizedVertices(convexHulls.get(regionName), size);
		return new Polygon(vertices);
	}
	
	private float[] sizedVertices(final float[] vertices, final Vector2 size) {
		Vector2 verticesSize = VertexUtils.bounds(vertices).getSize(new Vector2());
		float scaleX = size.x / verticesSize.x;
		float scaleY = size.y / verticesSize.y;
		float[] sizedVertices = new float[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			if (i % 2 == 0) {
				sizedVertices[i] = vertices[i] * scaleX;
			}
			else {
				sizedVertices[i] = vertices[i] * scaleY;
			}
		}
		return sizedVertices;
	}

}
