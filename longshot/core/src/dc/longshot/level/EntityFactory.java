package dc.longshot.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dc.longshot.epf.Entity;
import dc.longshot.geometry.ConvexHullCache;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.graphics.RegionFactory;
import dc.longshot.graphics.TextureCache;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.TransformPart;

public final class EntityFactory {
	
	private final TextureCache textureCache;
	private final ConvexHullCache convexHullCache;
	
	public EntityFactory(final TextureCache textureCache, final ConvexHullCache convexHullCache) {
		this.textureCache = textureCache;
		this.convexHullCache = convexHullCache;
	}
	
	public final Entity createBackgroundElement(final float[] vertices, final Vector3 position, final float minZ, 
			final String regionName) {
		Entity entity = new Entity();
		TextureRegion textureRegion = textureCache.getRegion(regionName);
		PolygonRegion region = RegionFactory.createPolygonRegion(textureRegion, vertices);
		DrawablePart drawablePart = new DrawablePart(new PolygonSprite(region), position.z);
		Color color = Color.WHITE.cpy().lerp(Color.DARK_GRAY, position.z / minZ);
		drawablePart.getSprite().setColor(color);
		entity.attach(drawablePart);
		float[] shiftedVertices = region.getVertices();
		float[] transformedVertices = new float[shiftedVertices.length];
		for (int i = 0; i < shiftedVertices.length; i++) {
			transformedVertices[i] = shiftedVertices[i] / UnitConvert.PIXELS_PER_UNIT;
		}
		entity.attach(new TransformPart(new Polygon(transformedVertices), new Vector2(position.x, position.y)));
		return entity;
	}
	
	public final Entity createBaseEntity(final Vector3 size, final Vector2 position, final String regionName) {
		Entity entity = new Entity();
		Polygon convexHull = convexHullCache.create(regionName, new Vector2(size.x, size.y));
		entity.attach(new TransformPart(convexHull, position));
		TextureRegion textureRegion = textureCache.getRegion(regionName);
		PolygonRegion region = RegionFactory.createPolygonRegion(textureRegion);
		entity.attach(new DrawablePart(new PolygonSprite(region), size.z));
		return entity;
	}
	
}
