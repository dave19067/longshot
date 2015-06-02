package dc.longshot.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dc.longshot.epf.Entity;
import dc.longshot.game.EntityUtils;
import dc.longshot.geometry.ConvexHullCache;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.graphics.TextureCache;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.util.FloatRange;

public final class EntityFactory {
	
	private final TextureCache textureCache;
	private final ConvexHullCache convexHullCache;
	
	public EntityFactory(final TextureCache textureCache, final ConvexHullCache convexHullCache) {
		this.textureCache = textureCache;
		this.convexHullCache = convexHullCache;
	}
	
	public final Entity createBackgroundElement(final Vector2 size, final Vector3 position, 
			final FloatRange zRange, final Color tint, final PolygonRegion region) {
		Entity entity = new Entity();
		PolygonSprite sprite = new PolygonSprite(region);
		DrawablePart drawablePart = new DrawablePart(sprite);
		Color color = tint.cpy().lerp(Color.BLACK, position.z / zRange.min());
		drawablePart.getSprite().setColor(color);
		entity.attach(drawablePart);
		float[] shiftedVertices = region.getVertices();
		float[] transformedVertices = new float[shiftedVertices.length];
		for (int i = 0; i < shiftedVertices.length; i++) {
			transformedVertices[i] = shiftedVertices[i] / UnitConvert.PIXELS_PER_UNIT;
		}
		TransformPart transformPart = new TransformPart(new Polygon(transformedVertices), position);
		float minZScale = 0.5f;
		Vector2 adjustedSize = EntityUtils.calculateSize(size, position.z, minZScale, zRange);
		transformPart.setSize(adjustedSize);
		entity.attach(transformPart);
		return entity;
	}
	
	public final Entity createBaseEntity(final Vector3 size, final Vector3 position, final String regionName) {
		Entity entity = new Entity();
		Polygon convexHull = convexHullCache.create(regionName, new Vector2(size.x, size.y));
		entity.attach(new TransformPart(convexHull, position));
		PolygonRegion region = textureCache.getPolygonRegion(regionName);
		entity.attach(new DrawablePart(new PolygonSprite(region)));
		return entity;
	}
	
}
