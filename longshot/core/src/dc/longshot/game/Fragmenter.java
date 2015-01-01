package dc.longshot.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.geometry.PolygonFactory;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.graphics.RegionFactory;
import dc.longshot.graphics.TextureUtils;
import dc.longshot.parts.ColorChangePart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TimedDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public final class Fragmenter {
	
	private final int fragWidth;
	private final int fragHeight;
	private final float fragSpeedMultiplier;
	private final Map<PolygonRegionKey, List<PolygonSprite>> regionToFragSprites
		= new HashMap<PolygonRegionKey, List<PolygonSprite>>();
	
	public Fragmenter(final int fragWidth, final int fragHeight, final float fragSpeedMultiplier) {
		this.fragWidth = fragWidth;
		this.fragHeight = fragHeight;
		this.fragSpeedMultiplier = fragSpeedMultiplier;
	}
	
	public final List<Entity> createFrags(final PolygonRegion region, final Polygon parentPolygon, final float z, 
			final float fadeTime) {
		List<PolygonSprite> fragSprites;
		PolygonRegionKey key = new PolygonRegionKey(region);
		if (regionToFragSprites.containsKey(key)) {
			fragSprites = regionToFragSprites.get(key);
		}
		else {
			fragSprites = createFragSprites(region, fragWidth, fragHeight);
		}
		List<Entity> frags = new ArrayList<Entity>();
		Vector2 scale = UnitConvert.worldToPixel(PolygonUtils.size(parentPolygon))
				.scl(1.0f / region.getRegion().getRegionWidth(), 1.0f / region.getRegion().getRegionHeight());
		for (PolygonSprite fragSprite : fragSprites) {
			Entity frag = createFrag(fragSprite, parentPolygon, scale, z, fadeTime);
			frags.add(frag);
		}
		return frags;
	}
	
	private final Entity createFrag(final PolygonSprite fragSprite, final Polygon parentPolygon, final Vector2 scale, 
			final float z, final float fadeTime) {
		Entity entity = new Entity();
		Polygon fragPolygon = new Polygon();
		fragPolygon.setRotation(parentPolygon.getRotation());
		Vector2 fragSize = UnitConvert.pixelToWorld(fragSprite.getWidth() * scale.x, fragSprite.getHeight() * scale.y);
		fragPolygon.setVertices(PolygonFactory.createRectangleVertices(fragSize.x, fragSize.y));
		Vector2 worldPosition = UnitConvert.pixelToWorld(fragSprite.getX() * scale.x, fragSprite.getY() * scale.y);
		Vector2 globalPosition = PolygonUtils.toGlobal(worldPosition.x, worldPosition.y, parentPolygon);
		entity.attach(new TransformPart(fragPolygon, globalPosition));
		PolygonSprite sprite = new PolygonSprite(fragSprite);
		entity.attach(new DrawablePart(sprite, z));
		Vector2 velocity = calculateVelocity(parentPolygon, fragPolygon);
		entity.attach(new SpeedPart(velocity.len()));
		TranslatePart translatePart = new TranslatePart();
		entity.attach(translatePart);
		translatePart.setDirection(velocity);
		entity.attach(new TimedDeathPart(fadeTime));
		entity.attach(new ColorChangePart(fadeTime, Color.WHITE.cpy(), Color.CLEAR.cpy()));
		return entity;
	}
	
	private final Vector2 calculateVelocity(final Polygon parentPolygon, final Polygon childPolygon) {
		Vector2 offset = VectorUtils.offset(PolygonUtils.center(parentPolygon), PolygonUtils.center(childPolygon));
		return VectorUtils.lengthened(offset, offset.len() * fragSpeedMultiplier);
	}
	
	private final List<PolygonSprite> createFragSprites(final PolygonRegion region, final int fragWidth, 
			final int fragHeight) {
		List<PolygonSprite> fragSprites = new ArrayList<PolygonSprite>();
		Pixmap pixmap = TextureUtils.toPixmap(region.getRegion());
		for (int x = 0; x < pixmap.getWidth(); x += fragWidth) {
			for (int y = 0; y < pixmap.getHeight(); y += fragHeight) {
				int width = Math.min(fragWidth, pixmap.getWidth() - x);
				int height = Math.min(fragHeight, pixmap.getHeight() - y);
				if (containsOpaquePixel(pixmap, x, y, width, height)) {
					TextureRegion croppedRegion = new TextureRegion(region.getRegion(), x, y, width, height);
					PolygonSprite sprite = new PolygonSprite(RegionFactory.createPolygonRegion(croppedRegion));
					sprite.setOrigin(0, 0);
					sprite.setPosition(x, pixmap.getHeight() - y - height);
					fragSprites.add(sprite);
				}
			}
		}
		regionToFragSprites.put(new PolygonRegionKey(region), fragSprites);
		return fragSprites;
	}
	
	private final boolean containsOpaquePixel(final Pixmap pixmap, final int left, final int bottom, 
			final int width, final int height) {
		for (int x = left; x < left + width; x++) {
			for (int y = bottom; y < bottom + height; y++) {
				if (!TextureUtils.isAlpha(pixmap.getPixel(x, y))) {
					return true;
				}
			}
		}
		return false;
	}
	
	private final class PolygonRegionKey {
		
		private final TextureRegion region;
		
		public PolygonRegionKey(final PolygonRegion region) {
			this.region = region.getRegion();
		}

		@Override
	    public final boolean equals(final Object obj) {
			if (obj instanceof PolygonRegionKey) {
				PolygonRegionKey other = ((PolygonRegionKey)obj);
				return region.getTexture() == other.region.getTexture()
					&& region.getRegionHeight() == other.region.getRegionHeight()
					&& region.getRegionWidth() == other.region.getRegionWidth()
					&& region.getRegionX() == other.region.getRegionX()
					&& region.getRegionY() == other.region.getRegionY()
					&& region.getU() == other.region.getU()
					&& region.getU2() == other.region.getU2()
					&& region.getV() == other.region.getV()
					&& region.getV2() == other.region.getV2();
			}
			return false;
		}

		@Override
		public final int hashCode() {
			return new HashCodeBuilder()
				.append(region.getTexture().getTextureObjectHandle())
				.append(region.getRegionHeight())
				.append(region.getRegionWidth())
				.append(region.getRegionX())
				.append(region.getRegionY())
				.append(region.getU())
				.append(region.getU2())
				.append(region.getV())
				.append(region.getV2())
				.toHashCode();
		}
		
	}
	
}
