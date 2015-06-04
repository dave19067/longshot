package dc.longshot.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dc.longshot.epf.Entity;
import dc.longshot.game.EntityUtils;
import dc.longshot.geometry.ConvexHullCache;
import dc.longshot.geometry.PolygonFactory;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.graphics.RegionFactory;
import dc.longshot.graphics.TextureCache;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.FragsPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.util.ColorUtils;
import dc.longshot.util.FloatRange;
import dc.longshot.util.IntRange;
import dc.longshot.util.Maths;

public final class EntityFactory {
	
	private final TextureCache textureCache;
	private final ConvexHullCache convexHullCache;
	private final Rectangle boundsBox;
	
	public EntityFactory(final TextureCache textureCache, final ConvexHullCache convexHullCache, 
			final Rectangle boundsBox) {
		this.textureCache = textureCache;
		this.convexHullCache = convexHullCache;
		this.boundsBox = boundsBox;
	}
	
	public final List<Entity> createBackgroundElements(final IntRange numRange, final IntRange columnsRange, 
			final IntRange rowsRange, final int cellWidth, final int cellHeight, final FloatRange zRange, 
			final TextureRegion windowsRegion, final PolygonRegion backRegion) {
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < numRange.random(); i++) {
			int numColumns = columnsRange.random();
			int numRows = rowsRange.random();
			int regionWidth = numColumns * cellWidth;
			int regionHeight = numRows * cellHeight;
			int regionX = Maths.round(MathUtils.random(0, windowsRegion.getRegionWidth() - regionWidth - 1), cellWidth);
			int regionY = Maths.round(MathUtils.random(0, windowsRegion.getRegionHeight() - regionHeight - 1), cellHeight);
			float[] vertices = PolygonFactory.createRectangleVertices(regionX, regionY, regionWidth, regionHeight);
			PolygonRegion region = RegionFactory.createPolygonRegion(windowsRegion, vertices);
			Vector2 size = new Vector2(numColumns * 0.6f, numRows * 0.8f);
			float x = MathUtils.random(-size.x, PolygonUtils.right(boundsBox));
			float z = zRange.random();
			Entity windows = createBackgroundElement(size, new Vector3(x, 0, z + 0.1f), zRange, Color.WHITE, 
					region);
			entities.add(windows);
			float h = MathUtils.random(0, ColorUtils.H_MAX);
			Color tint = ColorUtils.hsvToColor(h, 10, 25);
			Entity scraperBack = createBackgroundElement(size, new Vector3(x, 0, z), zRange, tint, 
					backRegion);
			entities.add(scraperBack);
		}
		return entities;
	}
	
	public final Entity createBaseEntity(final Vector3 size, final Vector3 position, final String regionName) {
		Entity entity = new Entity();
		Polygon convexHull = convexHullCache.create(regionName, new Vector2(size.x, size.y));
		entity.attach(new TransformPart(convexHull, position));
		PolygonRegion region = textureCache.getPolygonRegion(regionName);
		entity.attach(new DrawablePart(new PolygonSprite(region)));
		return entity;
	}
	
	private final Entity createBackgroundElement(final Vector2 size, final Vector3 position, 
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
		entity.attach(new FragsPart());
		return entity;
	}
	
}
